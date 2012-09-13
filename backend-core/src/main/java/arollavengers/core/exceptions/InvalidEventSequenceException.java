package arollavengers.core.exceptions;

import arollavengers.core.infrastructure.Id;

public class InvalidEventSequenceException extends InfrastructureRuntimeException {
  public InvalidEventSequenceException(final Id id, final long version, final long version1) {
  }
}
