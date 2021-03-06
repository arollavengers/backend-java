package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.Disease;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

public class WorldDiseaseEradicatedEvent implements PandemicEvent {

    @JsonProperty
    private final Id worldId;

    @JsonProperty
    private final Id memberId;

    @JsonProperty
    private final Disease disease;

    @JsonCreator
    public WorldDiseaseEradicatedEvent(@JsonProperty("worldId") Id worldId,
                                       @JsonProperty("memberId") Id memberId,
                                       @JsonProperty("disease") Disease disease)
    {
        this.worldId = worldId;
        this.memberId = memberId;
        this.disease = disease;
    }

    @Override
    public Id entityId() {
        throw new RuntimeException("not implemented");
    }

    public Disease disease() {
        return disease;
    }
}
