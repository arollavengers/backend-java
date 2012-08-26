package arollavengers.core.domain;

import arollavengers.core.events.WorldEvent;
import arollavengers.core.infrastructure.Id;

public class WorldCityTreatedEvent implements WorldEvent {

  private final Id worldId;
  private final Id memberId;
  private final CityId treatedCity;
  private final Disease treatedDisease;

  public WorldCityTreatedEvent(final Id worldId, final Id memberId, final CityId treatedCity, final Disease treatedDisease) {
    this.worldId = worldId;
    this.memberId = memberId;
    this.treatedCity = treatedCity;
    this.treatedDisease = treatedDisease;

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
    return treatedCity;
  }

  public Disease disease() {
    return treatedDisease;
  }
}
