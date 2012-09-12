package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.infrastructure.Id;

public class ResearchCenterBuiltEvent implements WorldEvent {
    private long version;

    private final Id worldId;
    private final CityId cityId;


    public ResearchCenterBuiltEvent(final Id worldId, final CityId cityId) {
        this.worldId = worldId;
        this.cityId = cityId;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public Id aggregateId() {
        return worldId;
    }

    @Override
    public void assignVersion(final long newVersion) {
        this.version = newVersion;
    }

    public CityId city() {
        return cityId;
    }
}
