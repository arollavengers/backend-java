package arollavengers.core.events;

import arollavengers.core.domain.CityId;
import arollavengers.core.domain.Disease;
import arollavengers.core.infrastructure.Id;

public class WorldCityCuredEvent implements WorldEvent {
  private final Id worldId;
  private final Id memberId;
  private final CityId city;
  private final Disease disease;

  public WorldCityCuredEvent(final Id worldId, final Id memberId, final CityId city, final Disease disease) {
    this.worldId = worldId;
    this.memberId = memberId;
    this.city = city;
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

  public CityId city() {
    return city;
  }

  public Disease disease() {
    return disease;
  }
}
