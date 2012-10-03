package arollavengers.core.infrastructure;

import arollavengers.pattern.annotation.CanBeInvokedOnlyOnce;

public interface DomainEvent {

    Id entityId();

}
