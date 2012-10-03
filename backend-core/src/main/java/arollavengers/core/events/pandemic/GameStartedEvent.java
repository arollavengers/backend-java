package arollavengers.core.events.pandemic;

import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

public class GameStartedEvent implements PandemicEvent {

    @JsonProperty
    private final Id worldId;

    @JsonCreator
    public GameStartedEvent(@JsonProperty("worldId") Id worldId) {
        this.worldId = worldId;
    }

    @Override
    public Id entityId() {
        return worldId;
    }

    @Override
    public String toString() {
        return "GameStartedEvent[" + worldId + "]";
    }

}
