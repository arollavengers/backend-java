package arollavengers.core.events.pandemic;

import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PlayerDrawPileCreatedEvent implements PandemicEvent {

    @JsonProperty
    private final Id worldId;

    @JsonProperty
    private final Id drawPileId;

    @JsonCreator
    public PlayerDrawPileCreatedEvent(@JsonProperty("worldId") Id worldId,
                                      @JsonProperty("drawPileId") Id drawPileId)
    {
        this.worldId = worldId;
        this.drawPileId = drawPileId;
    }

    @Override
    public Id entityId() {
        return worldId;
    }

    public Id drawPileId() {
        return drawPileId;
    }

    @Override
    public String toString() {
        return "PlayerDrawPileCreatedEvent[" + worldId +
                ", " + drawPileId +
                "]";
    }
}
