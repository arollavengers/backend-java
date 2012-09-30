package arollavengers.core.events.pandemic;

import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonTypeInfo;

public class WorldMemberActionSpentEvent implements PandemicEvent {

  public WorldMemberActionSpentEvent(final Object p0, final Object id) {
  }

  public long version() {
    throw new RuntimeException("not implemented");
  }

  public Id entityId() {
    throw new RuntimeException("not implemented");
  }

  public void assignVersion(final long l) {
    throw new RuntimeException("not implemented");
  }
}
