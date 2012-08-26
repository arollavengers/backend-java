package arollavengers.core.exceptions;

import arollavengers.core.domain.MemberRole;

public class WorldNumberOfRoleLimitReachedException extends RuntimeException {
  private final MemberRole extraRole;

  public WorldNumberOfRoleLimitReachedException(final MemberRole extraRole) {
    super("Cannot add role " + extraRole + " because limit is reached");
    this.extraRole = extraRole;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final WorldNumberOfRoleLimitReachedException that = (WorldNumberOfRoleLimitReachedException) o;

    if (extraRole != that.extraRole) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return extraRole != null ? extraRole.hashCode() : 0;
  }
}
