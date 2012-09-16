package arollavengers.core.infrastructure;

import arollavengers.core.exceptions.InvalidUnitOfWorkStateException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class DummyUnitOfWork implements UnitOfWork {

    private Map<Id, Uncommitted> uncommittedMap = Maps.newHashMap();
    private Map<Id, AggregateRoot<?>> attachedMap = Maps.newHashMap();

    @Override
    public void commit() {
        // c'est ici qu'il faut persister les events ?
        // ... ouep!
        for (Uncommitted uncommitted : uncommittedMap.values()) {
            uncommitted.commit();
        }
    }

    @Override
    public void rollback() {
        //TODO comment faire un rollback puisque tous les évents ont déjà été
        //appliqués? Faut-il rejouer tous les événements commités ??

        throw new RuntimeException("not implemented");
    }

    @Override
    public void registerNew(final DomainEvent event) {
        Id aggregateId = event.aggregateId();
        Uncommitted uncommitted = getOrCreateUncommitted(aggregateId);
        uncommitted.add(event);
    }

    private Uncommitted getOrCreateUncommitted(Id id) {
        Uncommitted uncommitted = uncommittedMap.get(id);
        if (uncommitted == null) {
            uncommitted = new Uncommitted(id);
            uncommittedMap.put(id, uncommitted);
        }
        return uncommitted;
    }

    /**
     * Return a copy of all uncommitted events for all aggregate roots.
     */
    public List<DomainEvent> getUncommitted() {
        List<DomainEvent> collected = Lists.newArrayList();
        for (Uncommitted uncommitted : uncommittedMap.values()) {
            collected.addAll(uncommitted.events);
        }
        return collected;
    }

    public void clearUncommitted() {
        uncommittedMap.clear();
    }

    @Override
    public void registerEventStoreFor(Id aggregateId, EventStore eventStore) {
        Uncommitted uncommitted = getOrCreateUncommitted(aggregateId);
        uncommitted.define(eventStore);
    }

    @Override
    public void attach(AggregateRoot<?> aggregateRoot) {
        attachedMap.put(aggregateRoot.aggregateId(), aggregateRoot);
    }

    @Override
    public void detach(AggregateRoot<?> aggregateRoot) {
        attachedMap.remove(aggregateRoot.aggregateId());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends AggregateRoot<?>> T getAggregate(Id id) {
        return (T) attachedMap.get(id);
    }

    private static class Uncommitted {
        private final Id aggregateId;
        private final List<DomainEvent> events = Lists.newArrayList();
        private EventStore eventStore;

        private Uncommitted(Id aggregateId) {
            this.aggregateId = aggregateId;
        }

        public void define(EventStore eventStore) {
            if (this.eventStore != null && this.eventStore != eventStore) {
                throw new InvalidUnitOfWorkStateException("A different event store has already been assigned");
            }
            this.eventStore = eventStore;
        }

        public void add(DomainEvent event) {
            events.add(event);
        }

        public void commit() {
            if (eventStore == null) {
                throw new InvalidUnitOfWorkStateException("No event store bound to " + aggregateId);
            }
            eventStore.store(aggregateId, Streams.from(events));
        }
    }
}
