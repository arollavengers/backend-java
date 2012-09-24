package arollavengers.core.infrastructure;


import arollavengers.core.exceptions.IncompatibleEventIdException;
import arollavengers.core.exceptions.InvalidEventSequenceException;
import arollavengers.core.util.Function;

import javax.annotation.Nonnull;

public abstract class AggregateRoot<E extends DomainEvent> {

    private long version = 0L;
    private Id id = Id.undefined();

    /**
     * @return the aggregate's id (may be {@link Id#undefined()}
     *         if the aggregate is not created yet)
     */
    public final Id aggregateId() {
        return id;
    }

    protected void assignId(Id newId) {
        this.id = newId;
    }

    /**
     * @return the aggregate's version
     */
    public long version() {
        return version;
    }

    /**
     * Entry point to take an new event into account.
     * This may lead to side effect within the aggregate boundaries,
     * such as states changes.
     *
     * @param event the new event to take into account
     */
    protected void applyNewEvent(E event) {
        applyEvent(event, true);
    }

    /**
     * Entry point to rebuild an aggregate from its history.
     *
     * @param stream Stream of events to rebuild state from
     */
    public void loadFromHistory(@Nonnull Stream<E> stream) {
        stream.consume(new Function<E>() {
            public void apply(E event) {
                applyEvent(event, false);
            }
        });
    }

    private void applyEvent(E event, boolean isNew) {
        checkOwnership(event);
        checkEventSequenceOrAssignIt(event, isNew);

        // invoke the event handler that may apply...
        // ...the event's side effect on the aggregate
        internalEventHandler().handle(event);

        // ensure aggregate version is the new one
        version = event.version();
        if (isNew) {
            unitOfWork().registerNew(event);
        }
    }

    private void checkOwnership(E event) {
        if (!id.isUndefined() && !id.equals(event.aggregateId())) {
            throw new IncompatibleEventIdException(this.id, event.aggregateId());
        }
    }

    private void checkEventSequenceOrAssignIt(E event, boolean isNew) {
        if (isNew) {
            event.assignVersion(version + 1);
        }
        else if (event.version() != (version + 1)) {
            throw new InvalidEventSequenceException(this.id, version, event.version());
        }
    }

    protected abstract EventHandler<E> internalEventHandler();

    /**
     * @return unit of work the aggregate belongs to.
     *         <p/>
     *         Default implementation usually relies on {@link ThreadLocal}.
     */
    protected abstract UnitOfWork unitOfWork();


}

