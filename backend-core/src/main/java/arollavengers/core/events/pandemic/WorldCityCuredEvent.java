package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.domain.pandemic.Disease;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class")
public class WorldCityCuredEvent implements WorldEvent {

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
                               @JsonProperty("city") CityId city, final Disease disease) {
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
