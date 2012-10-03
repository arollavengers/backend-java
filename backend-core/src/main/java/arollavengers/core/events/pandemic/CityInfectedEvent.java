package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.domain.pandemic.Disease;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CityInfectedEvent implements CityEvent {

    @JsonProperty
    private final Id worldId;

    @JsonProperty
    private final CityId cityId;

    @JsonProperty
    private final Disease disease;

    @JsonProperty
    private final Integer nbCubes;

    @JsonCreator
    public CityInfectedEvent(@JsonProperty("worldId") Id worldId,
                             @JsonProperty("cityId") CityId cityId,
                             @JsonProperty("disease") Disease disease,
                             @JsonProperty("nbCubes") Integer nbCubes)
    {
        this.worldId = worldId;
        this.cityId = cityId;
        this.disease = disease;
        this.nbCubes = nbCubes;
    }
    @Override
    public Id entityId() {
        return worldId;
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
        return "CityInfectedEvent[" + worldId + ", " + cityId + ", " + nbCubes + "]";
    }
}
