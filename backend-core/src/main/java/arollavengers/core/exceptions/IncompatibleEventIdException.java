package arollavengers.core.exceptions;

import arollavengers.core.infrastructure.Id;

public class IncompatibleEventIdException extends RuntimeException {
  public IncompatibleEventIdException(final Id id, final Id id1) {
  }
}
