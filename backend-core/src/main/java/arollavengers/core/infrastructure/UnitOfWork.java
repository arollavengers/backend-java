package arollavengers.core.infrastructure;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * <blockquote>A Unit of Work keeps track of everything you do during a business transaction
 * that can affect the database. When you're done, it figures out everything that needs to be
 * done to alter the database as a result of your work.</blockquote>
 * <br/>
 * See also:
 * <ul>
 * <li><a href="http://martinfowler.com/eaaCatalog/unitOfWork.html">Unit of Work (PEAA)</a></li>
 * <li><a href="http://msdn.microsoft.com/en-us/magazine/dd882510.aspx">The Unit Of Work Pattern And Persistence Ignorance</a></li>
 * </ul>
 */
public interface UnitOfWork {
    /**
     * Commits all the changes made to persistent objects to a data store.
     */
    void commit();

    void rollback();

    void registerNew(@Nonnull VersionedDomainEvent<?> event);

    /**
     * Register the event store that must be used to store the event for the given
     * aggregate root. This allows to use different event store within the same
     * unit of work. Event store can be different based on entity type.
     *
     * @param aggregateId aggregate root's id
     * @param eventStore event store used to store the aggregate's events.
     */
    void registerEventStoreFor(@Nonnull Id aggregateId, @Nonnull EventStore eventStore);

    /**
     * Attach the aggregate root to the unit of work, this ensure that for a given entity
     * (uniquely identified by its id) a sole instance exists within a unit of work. <br/>
     *
     * All newly created root domain objects must be registered to be inserted on commit.
     * Also any existing objects that will be edited and were not read from this unit of
     * work must also be registered.<br/>
     *
     * Once registered any changes to the objects will be committed to the database on commit.
     *
     * @param aggregateRoot aggregate root to attach
     * @see #getAggregate(Id)
     */
    void attach(@Nonnull AggregateRoot<?> aggregateRoot);

    void detach(@Nonnull Id aggregateId);

    /**
     *
     * @param id aggregate root's id.
     * @return the instance of the aggregate currently attached to the unit of work.
     * @see #attach(AggregateRoot)
     */
    @Nullable
    <T extends AggregateRoot<?>> T getAggregate(@Nonnull Id id);

}
