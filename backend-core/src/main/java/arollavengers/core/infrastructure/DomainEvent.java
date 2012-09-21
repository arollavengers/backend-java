package arollavengers.core.infrastructure;

import arollavengers.core.pattern.annotation.CanBeInvokedOnlyOnce;

public interface DomainEvent extends Message {
    long version();

    Id aggregateId();

    @CanBeInvokedOnlyOnce
    void assignVersion(long l);
}
