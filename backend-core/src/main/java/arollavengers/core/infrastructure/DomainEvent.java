package arollavengers.core.infrastructure;

import arollavengers.pattern.annotation.CanBeInvokedOnlyOnce;

public interface DomainEvent extends Message {
    long version();

    Id entityId();

    @CanBeInvokedOnlyOnce
    void assignVersion(long version);

}
