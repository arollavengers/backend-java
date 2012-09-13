package arollavengers.core.exceptions;

import arollavengers.core.infrastructure.Id;

public class EntityIdAlreadyAssignedException extends InfrastructureRuntimeException {
  private final Id oldId;
  private final Id newId;

  public EntityIdAlreadyAssignedException(final Id oldId, final Id newId) {
    super("World  id " + oldId + " cannot be reassigned with " + newId);
    this.oldId = oldId;
    this.newId = newId;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final EntityIdAlreadyAssignedException that = (EntityIdAlreadyAssignedException) o;

    if (newId != null ? !newId.equals(that.newId) : that.newId != null) return false;
    if (oldId != null ? !oldId.equals(that.oldId) : that.oldId != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = oldId != null ? oldId.hashCode() : 0;
    result = 31 * result + (newId != null ? newId.hashCode() : 0);
    return result;
  }
}
