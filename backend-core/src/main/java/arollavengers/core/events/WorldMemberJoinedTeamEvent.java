package arollavengers.core.events;

import arollavengers.core.domain.Member;
import arollavengers.core.infrastructure.Id;

public class WorldMemberJoinedTeamEvent implements WorldEvent {

  private long version;
  private final Id aggregateId;
  private final Member newComer;

  public WorldMemberJoinedTeamEvent(final Id aggregateId, final Member newComer) {
    this.aggregateId = aggregateId;
    this.newComer = newComer;
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

  public Member newComer() {
    return newComer;
  }
}
