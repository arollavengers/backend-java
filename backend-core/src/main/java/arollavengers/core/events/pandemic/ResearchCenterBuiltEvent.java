package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

public class ResearchCenterBuiltEvent implements PandemicEvent {

    @JsonProperty
    private long version;

    @JsonProperty
    private final Id worldId;

    @JsonProperty
    private final CityId cityId;

    @JsonCreator
    public ResearchCenterBuiltEvent(@JsonProperty("worldId") Id worldId,
                                    @JsonProperty("cityId") CityId cityId) {
        this.worldId = worldId;
        this.cityId = cityId;
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
    public void assignVersion(final long newVersion) {
        this.version = newVersion;
    }

    public CityId city() {
        return cityId;
    }

    @Override
    public String toString() {
        return "ResearchCenterBuiltEvent[" + worldId + ", v" + version + ", " + cityId + "]";
    }
}
