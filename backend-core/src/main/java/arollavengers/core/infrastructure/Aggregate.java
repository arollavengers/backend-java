package arollavengers.core.infrastructure;

import arollavengers.core.exceptions.EntityNotFoundInAggregateException;
import arollavengers.core.exceptions.InvalidEventSequenceException;
import arollavengers.core.util.Function;
import com.google.common.collect.Maps;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Aggregate<E extends DomainEvent> {

    private final Id aggregateRootId;
    private long version = 0L;
    private Map<Id, Entity<E>> entityMap = Maps.newHashMap();

    public Aggregate(Id aggregateRootId) {
        this.aggregateRootId = aggregateRootId;
    }

    /**
     * Register the entity within the aggregate.
     */
    public void register(@Nonnull Entity<E> entity) {
        entityMap.put(entity.entityId(), entity);
    }

    /**
     * @return the aggregate's version
     */
    public long version() {
        return version;
    }

    /**
     * Entry point to rebuild an aggregate from its history.
     *
     * @param stream Stream of events to rebuild state from
     */
    public void loadFromHistory(@Nonnull Stream<VersionedDomainEvent<E>> stream) {
        stream.consume(new Function<VersionedDomainEvent<E>>() {
            public void apply(VersionedDomainEvent<E> event) {
                applyEvent(event, false);
            }
        });
    }

    protected void applyEvent(@Nonnull E event, boolean isNew) {
        applyEvent(new VersionedDomainEvent<E>(aggregateRootId, event), isNew);
    }

    protected void applyEvent(@Nonnull VersionedDomainEvent<E> versionedEvent, boolean isNew) {
        checkEventSequenceOrAssignIt(versionedEvent, isNew);

        E event = versionedEvent.event();
        Entity<E> entity = getEntityOrFail(event);

        // invoke the versionedEvent handler that may apply...
        // ...the versionedEvent's side effect on the aggregate
        entity.internalEventHandler().handle(event);

        // ensure aggregate version is the new one
        version = versionedEvent.version();
        if (isNew) {
            unitOfWork().registerNew(versionedEvent);
        }
    }

    private UnitOfWork unitOfWork() {
        return aggregateRoot().unitOfWork();
    }

    private AggregateRoot<E> aggregateRoot() {
        return (AggregateRoot<E>)entityMap.get(aggregateRootId);
    }

    private Entity<E> getEntityOrFail(E event) {
        Entity<E> entity = entityMap.get(event.entityId());
        if(entity==null) {
            throw new EntityNotFoundInAggregateException(this.aggregateRootId, event.entityId());
        }
        return entity;
    }

    private void checkEventSequenceOrAssignIt(VersionedDomainEvent<E> versionedEvent, boolean isNew) {
        if (isNew) {
            versionedEvent.assignVersion(version + 1);
        }
        else if (versionedEvent.version() != (version + 1)) {
            throw new InvalidEventSequenceException(this.aggregateRootId, version, versionedEvent.version());
        }
    }

}
