package arollavengers.core.infrastructure.eventstore;

import arollavengers.core.exceptions.eventstore.MidAirCollisionException;
import arollavengers.core.infrastructure.DomainEvent;
import arollavengers.core.infrastructure.EventStore;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.Stream;
import arollavengers.core.infrastructure.Streams;
import arollavengers.core.util.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class EventStoreInMemory implements EventStore {

    private ConcurrentMap<Id, List<DomainEvent>> eventsPerStream = Maps.newConcurrentMap();

    @Override
    public void store(@Nonnull Id streamId, @Nonnull Stream<DomainEvent> stream) {
        List<DomainEvent> events = eventsPerStream.get(streamId);
        if (events == null) {
            List<DomainEvent> newEvents = Lists.newArrayList();
            events = eventsPerStream.putIfAbsent(streamId, newEvents);
            if (events == null) {
                events = newEvents;
            }
        }

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (events) {
            long lastVersion = 0;
            if (!events.isEmpty()) {
                DomainEvent lastEvent = events.get(events.size() - 1);
                lastVersion = lastEvent.version();
            }
            stream.consume(listAppender(streamId, lastVersion, events));
        }
    }

    @Override
    @Nullable
    public <E extends DomainEvent> Stream<E> openStream(@Nonnull Id streamId, @Nonnull Class<E> eventType) {
        List<DomainEvent> events = eventsPerStream.get(streamId);
        if (events == null) {
            //throw new StreamNotFoundException("Stream Id: " + streamId);
            return null;
        }
        return Streams.wrapAndCast(Streams.from(events));
    }

    private Function<DomainEvent> listAppender(final Id streamId,
                                               final long initialVersion,
                                               final List<DomainEvent> events)
    {
        return new Function<DomainEvent>() {
            private long lastVersion = initialVersion;

            @Override
            public void apply(DomainEvent domainEvent) {
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
        for (Map.Entry<Id, List<DomainEvent>> entry : eventsPerStream.entrySet()) {
            out.println(entry.getKey() + ": ");
            for (DomainEvent event : entry.getValue()) {
                out.println("  . " + event);
            }
        }
    }

    /**
     * Caution: return direct access to the underlying data structure.
     */
    public ConcurrentMap<Id, List<DomainEvent>> getEventsPerStream() {
        return eventsPerStream;
    }

    public void setEventsPerStream(Map<Id, List<DomainEvent>> eventsPerStream) {
        this.eventsPerStream.putAll(eventsPerStream);
    }
}
