package arollavengers.core.infrastructure;

import arollavengers.pattern.annotation.CanBeInvokedOnlyOnce;

public interface DomainEvent extends Message {
    long version();

    Id aggregateId();

    @CanBeInvokedOnlyOnce
    void assignVersion(long l);
}
