package arollavengers.core.infrastructure.eventstore;

import arollavengers.core.infrastructure.DomainEvent;
import arollavengers.core.infrastructure.EventStore;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.Stream;
import arollavengers.core.infrastructure.Streams;
import arollavengers.core.infrastructure.VersionedDomainEvent;
import arollavengers.pattern.annotation.DependencyInjection;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import javax.annotation.Nonnull;
import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.Query;
import org.prevayler.foundation.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class EventStorePrevayler implements EventStore {

    private Logger log = LoggerFactory.getLogger(EventStorePrevayler.class);

    private Prevayler prevayler;

    @Value("${event-store.prevayler.data-folder}")
    private String dataFolder;

    private arollavengers.core.infrastructure.Serializer serializer;

    @Override
    public void store(@Nonnull Id streamId, @Nonnull Stream<VersionedDomainEvent<?>> stream) {
        List<VersionedDomainEvent<?>> events = Streams.toList(stream);
        prevayler.execute(new AppendStream(streamId, events.toArray(new VersionedDomainEvent[events.size()])));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends DomainEvent> Stream<VersionedDomainEvent<E>> openStream(@Nonnull Id streamId, @Nonnull Class<E> eventType) {
        try {
            return (Stream<VersionedDomainEvent<E>>) prevayler.execute(new QueryStream(streamId));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dump(final PrintStream out) {
        try {
            prevayler.execute(new Query() {

                @Override
                public Object query(Object prevalentSystem, Date executionTime) {
                    PrevalentEventStore eventStore = (PrevalentEventStore) prevalentSystem;
                    for (Map.Entry<Id, List<VersionedDomainEvent>> entry : eventStore.eventsPerStream.entrySet()) {
                        out.println(entry.getKey() + ": ");
                        for (VersionedDomainEvent event : entry.getValue()) {
                            out.println("  . " + event);
                        }
                    }
                    return null;
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void postConstruct() throws Exception {
        if (dataFolder == null) {
            throw new BeanInitializationException("No data folder defined");
        }

        log.info("Prevayler data store folder: {}", dataFolder);

        PrevaylerFactory factory = new PrevaylerFactory();
        factory.configurePrevalentSystem(new PrevalentEventStore());
        factory.configurePrevalenceDirectory(dataFolder);
        factory.configureJournalSerializer("journal", adapt(serializer));
        prevayler = factory.create();
    }

    private Serializer adapt(final arollavengers.core.infrastructure.Serializer serializer) {
        return new Serializer() {
            @Override
            public void writeObject(OutputStream stream, Object object) throws Exception {
                serializer.writeObject(stream, object);
            }

            @Override
            public Object readObject(InputStream stream) throws Exception {
                return serializer.readObject(stream, Object.class);
            }
        };
    }

    @DependencyInjection
    public void setDataFolder(String dataFolder) {
        this.dataFolder = dataFolder;
    }

    @DependencyInjection
    public void setSerializer(arollavengers.core.infrastructure.Serializer serializer) {
        this.serializer = serializer;
    }

    public static final class PrevalentEventStore implements Serializable {
        private Map<Id, List<VersionedDomainEvent>> eventsPerStream = Maps.newHashMap();

        public void appendOrCreate(Id streamId, VersionedDomainEvent... stream) {
            appendOrCreate(streamId, Arrays.asList(stream));
        }

        public void appendOrCreate(Id streamId, List<VersionedDomainEvent> stream) {
            List<VersionedDomainEvent> events = eventsPerStream.get(streamId);
            if (events == null) {
                events = Lists.newArrayList();
                eventsPerStream.put(streamId, events);
            }
            events.addAll(stream);
        }

        public <E extends DomainEvent> Stream<VersionedDomainEvent<E>> openStream(Id streamId) {
            List<VersionedDomainEvent> events = eventsPerStream.get(streamId);
            if (events == null) {
                //throw new StreamNotFoundException("Stream Id: " + streamId);
                return null;
            }
            return Streams.wrapAndCast(Streams.from(events));
        }
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.CLASS,
            include = JsonTypeInfo.As.PROPERTY,
            property = "@class")
    public static class QueryStream implements Query {
        private final Id streamId;

        public QueryStream(Id streamId) {
            this.streamId = streamId;
        }

        @Override
        public Object query(Object prevalentSystem, Date executionTime) {
            PrevalentEventStore eventStore = (PrevalentEventStore) prevalentSystem;
            return eventStore.openStream(streamId);
        }
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.CLASS,
            include = JsonTypeInfo.As.PROPERTY,
            property = "@class")
    public static class AppendStream implements org.prevayler.Transaction {

        @JsonProperty
        private final Id streamId;

        @JsonProperty
        private final VersionedDomainEvent[] events;

        @JsonCreator
        public AppendStream(
                @JsonProperty("streamId") Id streamId,
                @JsonProperty("events") VersionedDomainEvent[] events)
        {
            this.streamId = streamId;
            this.events = events;
        }

        @Override
        public void executeOn(Object prevalentSystem, Date executionTime) {
            PrevalentEventStore eventStore = (PrevalentEventStore) prevalentSystem;
            eventStore.appendOrCreate(streamId, events);
        }
    }
}
