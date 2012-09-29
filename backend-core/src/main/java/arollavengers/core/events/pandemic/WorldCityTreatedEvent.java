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
public class WorldCityTreatedEvent implements PandemicEvent {

    @JsonProperty
    private final Id worldId;

    @JsonProperty
    private final Id memberId;

    @JsonProperty
    private final CityId treatedCity;

    @JsonProperty
    private final Disease treatedDisease;

    @JsonCreator
    public WorldCityTreatedEvent(@JsonProperty("worldId") Id worldId,
                                 @JsonProperty("memberId") Id memberId,
                                 @JsonProperty("treatedCity") CityId treatedCity,
                                 @JsonProperty("treatedDisease") Disease treatedDisease)
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
