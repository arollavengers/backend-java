package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.domain.pandemic.Disease;
import arollavengers.core.infrastructure.Id;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CityInfectedEvent implements CityEvent {

    private long version;
    private final Id worldId;
    private final CityId cityId;
    private final Disease disease;
    private final Integer nbCubes;

    public CityInfectedEvent(Id worldId, CityId cityId, Disease disease, Integer nbCubes) {
        this.worldId = worldId;
        this.cityId = cityId;
        this.disease = disease;
        this.nbCubes = nbCubes;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public Id entityId() {
        return worldId;
    }

    @Override
    public void assignVersion(long version) {
        this.version = version;
    }

    public Disease disease() {
        return disease;
    }

    public CityId cityId() {
        return cityId;
    }

    public Integer nbCubes() {
        return nbCubes;
    }

    @Override
    public String toString() {
        return "CityInfectedEvent[" + worldId + ", v" + version + ", " + cityId + ", " + nbCubes + "]";
    }
}
