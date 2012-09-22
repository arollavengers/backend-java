package arollavengers.core.exceptions;

import arollavengers.core.infrastructure.Id;

public class EntityNotFoundInAggregateException extends InfrastructureRuntimeException {
  public EntityNotFoundInAggregateException(Id aggregateRootId, Id entityId) {
      super("Entity with id " + entityId + " was not found in aggregate with root " + aggregateRootId);
  }
}
