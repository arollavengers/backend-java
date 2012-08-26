package arollavengers.core.events;

import arollavengers.core.domain.Disease;
import arollavengers.core.infrastructure.Id;

public class WorldDiseaseEradicatedEvent implements WorldEvent {

  private final Id worldId;
  private final Id memberId;
  private final Disease disease;

  public WorldDiseaseEradicatedEvent(final Id worldId, final Id memberId, final Disease disease) {
    this.worldId = worldId;
    this.memberId = memberId;
    this.disease = disease;
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

  public Disease disease() {
    return disease;
  }
}
