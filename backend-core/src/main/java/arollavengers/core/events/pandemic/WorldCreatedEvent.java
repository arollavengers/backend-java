package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.Difficulty;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class")
public class WorldCreatedEvent implements PandemicEvent {

    @JsonProperty
    private long version;

    @JsonProperty
    private final Id newWorldId;

    @JsonProperty
    private final Id ownerId;

    @JsonProperty
    private final Difficulty difficulty;

    @JsonCreator
    public WorldCreatedEvent(@JsonProperty("newWorldId") Id newWorldId,
                             @JsonProperty("ownerId") Id ownerId,
                             @JsonProperty("difficulty") Difficulty difficulty) {
        this.newWorldId = newWorldId;
        this.ownerId = ownerId;
        this.difficulty = difficulty;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public void assignVersion(final long version) {
        this.version = version;
    }

    @Override
    public Id entityId() {
        return newWorldId;
    }

    public Difficulty difficulty() {
        return difficulty;
    }

    public Id ownerId() {
        return ownerId;
    }

    @Override
    public String toString() {
        return "WorldCreatedEvent[" + newWorldId + ", v" + version + ", owner: " + ownerId + ", difficulty: " + difficulty + "]";
    }


}
