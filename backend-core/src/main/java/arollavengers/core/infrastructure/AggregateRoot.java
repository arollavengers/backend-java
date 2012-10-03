package arollavengers.core.infrastructure;


import javax.annotation.Nonnull;

public abstract class AggregateRoot<E extends DomainEvent> extends Entity<E> {

    protected AggregateRoot(@Nonnull Id entityId) {
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
    public void loadFromHistory(@Nonnull Stream<VersionedDomainEvent<E>> stream) {
        aggregate().loadFromHistory(stream);
    }

    /**
     * Returns the current unit of work this aggregate root belongs to.
     */
    protected abstract UnitOfWork unitOfWork();
}
