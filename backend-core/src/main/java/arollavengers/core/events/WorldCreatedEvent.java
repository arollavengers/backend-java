package arollavengers.core.events;

import arollavengers.core.domain.Difficulty;
import arollavengers.core.domain.UserId;
import arollavengers.core.infrastructure.Id;

public class WorldCreatedEvent implements WorldEvent {

  private long version;

  private final Id newId;
  private final UserId userId;
  private final Difficulty difficulty;

  public WorldCreatedEvent(final Id newId, final UserId userId, final Difficulty difficulty) {
    this.newId = newId;
    this.userId = userId;
    this.difficulty = difficulty;
  }

  public long version() {
    return 0L;
  }

  public void assignVersion(final long version) {
    this.version = version;
  }

  public Id aggregateId() {
    return newId;
  }

  public Difficulty difficulty() {
    return difficulty;
  }

  public UserId ownerId() {
    return userId;
  }
}
