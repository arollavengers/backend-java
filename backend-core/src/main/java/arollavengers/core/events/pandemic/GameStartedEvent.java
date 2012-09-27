package arollavengers.core.events.pandemic;

import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class")
public class GameStartedEvent implements WorldEvent {

    @JsonProperty
    private long version;

    @JsonProperty
    private final Id worldId;

    @JsonCreator
    public GameStartedEvent(@JsonProperty("worldId") Id worldId) {
        this.worldId = worldId;
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
    public void assignVersion(final long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "GameStartedEvent[" + worldId + ", v" + version + "]";
    }

}
