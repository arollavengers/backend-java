package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.MemberRole;
import arollavengers.core.infrastructure.Id;

public class WorldMemberJoinedTeamEvent implements WorldEvent {

  private long version;
  private final Id aggregateId;
  private final Id newComerId;
  private final MemberRole role;

  public WorldMemberJoinedTeamEvent(final Id aggregateId, final Id newComerId, final MemberRole role) {
    this.aggregateId = aggregateId;
    this.newComerId = newComerId;
    this.role = role;
  }

  public long version() {
    return version;
  }

  public void assignVersion(final long version) {
    this.version = version;
  }

  public Id aggregateId() {
    return aggregateId;
  }

  public Id newComerId() {
    return newComerId;
  }

  public MemberRole role() {
    return role;
  }
}
