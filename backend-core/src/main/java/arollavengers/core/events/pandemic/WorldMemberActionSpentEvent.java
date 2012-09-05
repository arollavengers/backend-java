package arollavengers.core.events.pandemic;

import arollavengers.core.infrastructure.Id;

public class WorldMemberActionSpentEvent implements WorldEvent {
  public WorldMemberActionSpentEvent(final Object p0, final Object id) {
  }

  public long version() {
    throw new RuntimeException("not implemented");
  }

  public Id aggregateId() {
    throw new RuntimeException("not implemented");
  }

  public void assignVersion(final long l) {
    throw new RuntimeException("not implemented");
  }
}
