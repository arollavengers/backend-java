package arollavengers.core.infrastructure;

import org.jetbrains.annotations.NotNull;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public abstract class Entity<E extends DomainEvent> {

    private final Aggregate<E> aggregate;
    private final Id entityId;

    public Entity(@NotNull Aggregate<E> aggregate, @NotNull Id entityId) {
        this.aggregate = aggregate;
        this.entityId = entityId;
        this.aggregate.register(this);
    }

    /**
     * @return the aggregate this entity belongs to.
     */
    protected Aggregate<E> aggregate() {
        return aggregate;
    }

    /**
     * @return the entity's id (may be {@link Id#undefined()}
     *         if the entity is not created yet)
     */
    public final Id entityId() {
        return entityId;
    }

    /**
     * Entry point to take an new event into account.
     * This may lead to side effect within the aggregate boundaries,
     * such as states changes.
     *
     * @param event the new event to take into account
     */
    protected void applyNewEvent(E event) {
        aggregate().applyEvent(event, true);
    }

    protected abstract EventHandler<E> internalEventHandler();
}
