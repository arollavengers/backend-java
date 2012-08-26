package arollavengers.core.exceptions;

import arollavengers.core.domain.MemberRole;
import arollavengers.core.infrastructure.Id;

public class WorldRoleAlreadyChosenException extends RuntimeException {
  private final Id aggregateId;
  private final MemberRole duplicatedRole;

  public WorldRoleAlreadyChosenException(final Id aggregateId, final MemberRole duplicatedRole) {
    super("Role " + duplicatedRole + " duplicated in world " + aggregateId);
    this.aggregateId = aggregateId;
    this.duplicatedRole = duplicatedRole;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final WorldRoleAlreadyChosenException that = (WorldRoleAlreadyChosenException) o;

    if (aggregateId != null ? !aggregateId.equals(that.aggregateId) : that.aggregateId != null) return false;
    if (duplicatedRole != that.duplicatedRole) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = aggregateId != null ? aggregateId.hashCode() : 0;
    result = 31 * result + (duplicatedRole != null ? duplicatedRole.hashCode() : 0);
    return result;
  }
}
