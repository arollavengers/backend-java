package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.domain.pandemic.Disease;
import arollavengers.core.infrastructure.Id;

public class WorldCityTreatedEvent implements WorldEvent {

    private final Id worldId;
    private final Id memberId;
    private final CityId treatedCity;
    private final Disease treatedDisease;

    public WorldCityTreatedEvent(final Id worldId,
                                 final Id memberId,
                                 final CityId treatedCity,
                                 final Disease treatedDisease)
    {
        this.worldId = worldId;
        this.memberId = memberId;
        this.treatedCity = treatedCity;
        this.treatedDisease = treatedDisease;
    }

    @Override
    public long version() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Id entityId() {
        throw new RuntimeException("not implemented");
    }

    @Override
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
