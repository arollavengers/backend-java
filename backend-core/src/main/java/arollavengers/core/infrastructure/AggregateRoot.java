package arollavengers.core.infrastructure;


import org.jetbrains.annotations.NotNull;

public abstract class AggregateRoot<E extends DomainEvent> extends Entity<E> {

    protected AggregateRoot(Id entityId) {
        super(new Aggregate<E>(entityId), entityId);
    }

    /**
     * @return the aggregate's version
     */
    public long version() {
        return aggregate().version();
    }

    /**
     * Entry point to rebuild an aggregate from its history.
     *
     * @param stream Stream of events to rebuild state from
     */
    public void loadFromHistory(@NotNull Stream<E> stream) {
        aggregate().loadFromHistory(stream);
    }

    protected abstract UnitOfWork unitOfWork();
}
