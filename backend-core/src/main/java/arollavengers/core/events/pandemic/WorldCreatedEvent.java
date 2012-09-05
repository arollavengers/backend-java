package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.Difficulty;
import arollavengers.core.infrastructure.Id;

public class WorldCreatedEvent implements WorldEvent {

  private long version;

  private final Id newWorldId;
  private final Id userId;
  private final Difficulty difficulty;

  public WorldCreatedEvent(final Id newWorldId, final Id userId, final Difficulty difficulty) {
    this.newWorldId = newWorldId;
    this.userId = userId;
    this.difficulty = difficulty;
  }

  public long version() {
    return version;
  }

  public void assignVersion(final long version) {
    this.version = version;
  }

  public Id aggregateId() {
    return newWorldId;
  }

  public Difficulty difficulty() {
    return difficulty;
  }

  public Id ownerId() {
    return userId;
  }
}
