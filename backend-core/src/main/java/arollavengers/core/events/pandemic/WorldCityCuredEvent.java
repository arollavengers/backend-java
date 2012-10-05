package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.domain.pandemic.Disease;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

public class WorldCityCuredEvent implements PandemicEvent {

    @JsonProperty
    private final Id worldId;

    @JsonProperty
    private final Id memberId;

    @JsonProperty
    private final CityId city;

    @JsonProperty
    private final Disease disease;

    @JsonCreator
    public WorldCityCuredEvent(@JsonProperty("worldId") Id worldId,
                               @JsonProperty("memberId") Id memberId,
                               @JsonProperty("cityId") CityId city, final Disease disease) {
        this.worldId = worldId;
        this.memberId = memberId;
        this.city = city;
        this.disease = disease;

    }

    @Override
    public Id entityId() {
        return worldId;
    }

    public CityId city() {
        return city;
    }

    public Disease disease() {
        return disease;
    }
}
