package arollavengers.core.infrastructure;

import arollavengers.core.exceptions.InvalidUnitOfWorkStateException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public class UnitOfWorkDefault implements UnitOfWork {

    private final Map<Id, Uncommitted> uncommittedMap = Maps.newHashMap();
    private final Map<Id, AggregateRoot<?>> attachedMap = Maps.newHashMap();
    private final List<VersionedDomainEvent> unpublishedMessages = Lists.newArrayList();
    private final Bus bus;

    @Inject
    public UnitOfWorkDefault(Bus bus) {
        this.bus = bus;
    }


    @Override
    public void commit() {
        // c'est ici qu'il faut persister les events ?
        // ... ouep!
        commitAllUncommited();
        publishAllUnpublished();
    }

    private void publishAllUnpublished() {
        for(Message message: unpublishedMessages) {
            bus.publish(message);
        }
        unpublishedMessages.clear();
    }

    private void commitAllUncommited() {
        for (Uncommitted uncommitted : uncommittedMap.values()) {
            uncommitted.commit();
        }
        uncommittedMap.clear();
    }

    @Override
    public void rollback() {
        //TODO comment faire un rollback puisque tous les évents ont déjà été
        //appliqués? Faut-il rejouer tous les événements commités ??

        throw new RuntimeException("not implemented");
    }

    @Override
    public void registerNew(@Nonnull VersionedDomainEvent versionedEvent) {
        Uncommitted uncommitted = getOrCreateUncommitted(versionedEvent.aggregateId());
        uncommitted.add(versionedEvent);
        unpublishedMessages.add(versionedEvent);
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
    public List<VersionedDomainEvent<?>> getAllUncommitted() {
        List<VersionedDomainEvent<?>> collected = Lists.newArrayList();
        for (Uncommitted uncommitted : uncommittedMap.values()) {
            collected.addAll(uncommitted.events);
        }
        return collected;
    }

    public void clearUncommitted() {
        uncommittedMap.clear();
    }

    @Override
    public void registerEventStoreFor(@Nonnull Id aggregateId, @Nonnull EventStore eventStore) {
        Uncommitted uncommitted = getOrCreateUncommitted(aggregateId);
        uncommitted.define(eventStore);
    }

    @Override
    public void attach(@Nonnull AggregateRoot<?> aggregateRoot) {
        attachedMap.put(aggregateRoot.entityId(), aggregateRoot);
    }

    @Override
    public void detach(@Nonnull Id aggregateId) {
        attachedMap.remove(aggregateId);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T extends AggregateRoot<?>> T getAggregate(@Nonnull Id id) {
        return (T) attachedMap.get(id);
    }

    private static class Uncommitted {
        private final Id aggregateId;
        private final List<VersionedDomainEvent<?>> events = Lists.newArrayList();
        private EventStore eventStore;

        private Uncommitted(Id aggregateId) {
            if(aggregateId.isUndefined())
                throw new RuntimeException("Id cannot be undefined here!");
            this.aggregateId = aggregateId;
        }

        public void define(EventStore eventStore) {
            if (this.eventStore != null && this.eventStore != eventStore) {
                throw new InvalidUnitOfWorkStateException("A different event store has already been assigned");
            }
            this.eventStore = eventStore;
        }

        public void add(VersionedDomainEvent<?> event) {
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
