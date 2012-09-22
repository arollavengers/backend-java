package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.domain.pandemic.Disease;
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

    @Override
    public long version() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Id entityId() {
        return worldId;
    }

    @Override
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
