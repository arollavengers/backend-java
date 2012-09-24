package arollavengers.core.exceptions;

import arollavengers.core.infrastructure.Id;

public class InvalidEventSequenceException extends InfrastructureRuntimeException {
  public InvalidEventSequenceException(final Id aggregateRootId, final long actualVersion, final long eventVersion) {
      super("The sequence of event is invalid for aggregate root " + aggregateRootId + " actual version: " + actualVersion + " but received event with version: " + eventVersion);
  }
}
