package arollavengers.core.events.pandemic;

import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class")
public class InfectionDrawPileCreatedEvent implements WorldEvent {
    @JsonProperty
    private final Id worldId;

    @JsonProperty
    private final Id drawPileId;

    @JsonProperty
    private long version;

    @JsonCreator
    public InfectionDrawPileCreatedEvent(@JsonProperty("worldId") Id worldId,
                                         @JsonProperty("drawPileId") Id drawPileId)
    {
        this.worldId = worldId;
        this.drawPileId = drawPileId;
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

    public Id drawPileId() {
        return drawPileId;
    }

    @Override
    public String toString() {
        return "InfectionDrawPileCreatedEvent[" + worldId +
                ", v" + version +
                ", " + drawPileId +
                "]";
    }
}
