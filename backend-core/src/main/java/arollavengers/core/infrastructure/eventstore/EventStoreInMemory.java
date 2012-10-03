package arollavengers.core.infrastructure.eventstore;

import arollavengers.core.exceptions.eventstore.MidAirCollisionException;
import arollavengers.core.infrastructure.DomainEvent;
import arollavengers.core.infrastructure.EventStore;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.Stream;
import arollavengers.core.infrastructure.Streams;
import arollavengers.core.infrastructure.VersionedDomainEvent;
import arollavengers.core.util.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.springframework.stereotype.Repository;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Repository
public class EventStoreInMemory implements EventStore {

    private ConcurrentMap<Id, List<VersionedDomainEvent>> eventsPerStream = Maps.newConcurrentMap();

    @Override
    public void store(@Nonnull Id streamId, @Nonnull Stream<VersionedDomainEvent<?>> stream) {
        List<VersionedDomainEvent> events = eventsPerStream.get(streamId);
        if (events == null) {
            List<VersionedDomainEvent> newEvents = Lists.newArrayList();
            events = eventsPerStream.putIfAbsent(streamId, newEvents);
            if (events == null) {
                events = newEvents;
            }
        }

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (events) {
            long lastVersion = 0;
            if (!events.isEmpty()) {
                VersionedDomainEvent lastEvent = events.get(events.size() - 1);
                lastVersion = lastEvent.version();
            }
            stream.consume(listAppender(streamId, lastVersion, events));
        }
    }

    @Override
    @Nullable
    public <E extends DomainEvent> Stream<VersionedDomainEvent<E>> openStream(@Nonnull Id streamId, @Nonnull Class<E> eventType) {
        List<VersionedDomainEvent> events = eventsPerStream.get(streamId);
        if (events == null) {
            //throw new StreamNotFoundException("Stream Id: " + streamId);
            return null;
        }
        return Streams.wrapAndCast(Streams.from(events));
    }

    private static Function<VersionedDomainEvent<?>> listAppender(final Id streamId,
                                               final long initialVersion,
                                               final List<VersionedDomainEvent> events)
    {
        return new Function<VersionedDomainEvent<?>>() {
            private long lastVersion = initialVersion;

            @Override
            public void apply(VersionedDomainEvent domainEvent) {
                if (domainEvent.version() != (lastVersion + 1)) {
                    throw new MidAirCollisionException(
                            "Stream Id: " + streamId + ", expected version: " + (lastVersion + 1) + " but got: "
                                    + domainEvent.version());
                }
                events.add(domainEvent);
                lastVersion++;
            }
        };
    }

    public void dump(PrintStream out) {
        for (Map.Entry<Id, List<VersionedDomainEvent>> entry : eventsPerStream.entrySet()) {
            out.println(entry.getKey() + ": ");
            for (VersionedDomainEvent event : entry.getValue()) {
                out.println("  . " + event);
            }
        }
    }

    /**
     * Caution: return direct access to the underlying data structure.
     */
    public ConcurrentMap<Id, List<VersionedDomainEvent>> getEventsPerStream() {
        return eventsPerStream;
    }
}
