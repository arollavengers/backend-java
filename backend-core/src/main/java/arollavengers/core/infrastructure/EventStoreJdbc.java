package arollavengers.core.infrastructure;

import arollavengers.core.exceptions.eventstore.MidAirCollisionException;
import arollavengers.core.pattern.annotation.DependencyInjection;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class EventStoreJdbc implements EventStore {

    private Logger logger = LoggerFactory.getLogger(EventStoreJdbc.class);

    @Inject
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Inject
    private Serializer serializer;

    @DependencyInjection
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @DependencyInjection
    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    @PostConstruct
    public void postConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void store(@NotNull final Id streamId, @NotNull final Stream<DomainEvent> stream) {
        jdbcTemplate.execute(new ConnectionCallback<Object>() {
            @Override
            public Object doInConnection(Connection connection) throws SQLException, DataAccessException {
                connection.setAutoCommit(false);
                boolean ok = false;
                try {
                    store(connection, streamId, stream);
                    ok = true;
                }
                catch (IOException e) {
                    // TODO a better exception
                    throw new RuntimeException("Oops", e);
                }
                finally {
                    if (ok) {
                        connection.commit();
                    }
                    else {
                        connection.rollback();
                    }
                }
                return null;
            }
        });
    }

    private void store(Connection connection, Id streamId, Stream<DomainEvent> stream) throws SQLException,
                                                                                              IOException
    {
        List<DomainEvent> events = Streams.toList(stream);
        if (events.isEmpty()) {
            logger.warn("No events to store for stream {}", streamId);
            return;
        }

        DomainEvent firstEvent = events.get(0);
        DomainEvent lastEvent = events.get(events.size() - 1);

        // if it is the first event of the aggregate, it is a new aggregate
        if (firstEvent.version() == 1) {
            PreparedStatement pStmt = connection.prepareStatement("insert into streams (stream_id, stream_version) values (?,?)");
            try {
                pStmt.setString(1, streamId.toUUID());
                pStmt.setLong(2, lastEvent.version());
                if (pStmt.executeUpdate() != 1)
                //TODO better exception!
                {
                    throw new RuntimeException("Failed to insert stream");
                }
            }
            finally {
                JdbcUtils.closeStatement(pStmt);
            }
        }
        else {
            PreparedStatement pStmt = connection.prepareStatement("update streams set stream_version = ? where stream_id = ? and stream_version = ?");
            try {
                pStmt.setLong(1, lastEvent.version());
                pStmt.setString(2, streamId.toUUID());
                pStmt.setLong(3, firstEvent.version() - 1);
                if (pStmt.executeUpdate() != 1)
                //TODO better exception!
                {
                    throw new MidAirCollisionException("Failed to update stream");
                }
            }
            finally {
                JdbcUtils.closeStatement(pStmt);
            }
        }

        PreparedStatement pStmt = connection.prepareStatement("insert into stream_events (stream_id, event_id, event_data) values (?,?,?)");
        try {
            for (DomainEvent event : events) {
                String eventAsString = serializer.serializeAsString(event);

                pStmt.setString(1, streamId.toUUID());
                pStmt.setLong(2, event.version());
                pStmt.setString(3, eventAsString);
                if (pStmt.executeUpdate() != 1)
                //TODO better exception!
                {
                    throw new RuntimeException("Failed to insert stream's event");
                }
                pStmt.clearParameters();
            }
        }
        finally {
            JdbcUtils.closeStatement(pStmt);
        }
    }

    @Override
    public <E extends DomainEvent> Stream<E> openStream(@NotNull Id streamId, Class<E> eventType) {
        String sql = "select stream_id, event_id, event_data from stream_events where stream_id = ? order by event_id";
        List<E> events = jdbcTemplate.query(sql, new RowMapper<E>() {
            @SuppressWarnings("unchecked")
            @Override
            public E mapRow(ResultSet rs, int rowNum) throws SQLException {
                String eventData = rs.getString(3);
                try {
                    return (E) serializer.deserializeFomString(eventData);
                }
                catch (Serializer.SerializationException e) {
                    // TODO better exception...
                    throw new RuntimeException("Failed to deserialize event from content: " + eventData, e);
                }
            }
        }, streamId.toUUID());
        return Streams.from(events);
    }

    @Override
    public void dump(final PrintStream out) {
        jdbcTemplate.query("select stream_id, event_id, event_data from stream_events order by stream_id, event_id", new RowCallbackHandler() {
            private final String repl = "\n" + StringUtils.repeat(" ", 36) + "|" + StringUtils.repeat(" ", 6) + "|";

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                out.print(StringUtils.rightPad(rs.getString(1), 36, '.'));
                out.print("|");
                out.print(StringUtils.leftPad("" + rs.getLong(2), 6, '.'));
                out.print("|");
                out.print(rs.getString(3).replace("\n", repl));
                out.println();
            }
        });
    }
}
