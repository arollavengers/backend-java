package arollavengers.core.exceptions;

import arollavengers.core.infrastructure.Id;

public class EntityIdAlreadyAssignedException extends RuntimeException {
  public EntityIdAlreadyAssignedException(final Id id, final Id newId) {
  }
}
