package arollavengers.core.infrastructure;

import arollavengers.core.exceptions.EntityNotFoundInAggregateException;
import arollavengers.core.exceptions.InvalidEventSequenceException;
import arollavengers.core.util.Function;
import com.google.common.collect.Maps;

import org.jetbrains.annotations.NotNull;
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
     *
     * @param entity
     */
    public void register(Entity<E> entity) {
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
    public void loadFromHistory(@NotNull Stream<E> stream) {
        stream.consume(new Function<E>() {
            public void apply(E event) {
                applyEvent(event, false);
            }
        });
    }

    protected void applyEvent(E event, boolean isNew) {
        checkEventSequenceOrAssignIt(event, isNew);

        Entity<E> entity = getEntityOrFail(event);

        // invoke the event handler that may apply...
        // ...the event's side effect on the aggregate
        entity.internalEventHandler().handle(event);

        // ensure aggregate version is the new one
        version = event.version();
        if (isNew) {
            unitOfWork().registerNew(aggregateRootId, event);
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

    private void checkEventSequenceOrAssignIt(E event, boolean isNew) {
        if (isNew) {
            event.assignVersion(version + 1);
        }
        else if (event.version() != (version + 1)) {
            throw new InvalidEventSequenceException(this.aggregateRootId, version, event.version());
        }
    }

}
