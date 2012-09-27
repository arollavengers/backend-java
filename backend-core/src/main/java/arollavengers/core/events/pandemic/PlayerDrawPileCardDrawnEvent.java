package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.PlayerCard;
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
public class PlayerDrawPileCardDrawnEvent implements PlayerDrawPileEvent {

    @JsonProperty
    private final Id drawPileId;

    @JsonProperty
    private final PlayerCard cardDrawn;

    @JsonProperty
    private long version;

    @JsonCreator
    public PlayerDrawPileCardDrawnEvent(@JsonProperty("drawPileId") Id drawPileId,
                                        @JsonProperty("cardDrawn") PlayerCard cardDrawn)
    {
        this.drawPileId = drawPileId;
        this.cardDrawn = cardDrawn;
    }

    @Override
    public Id entityId() {
        return drawPileId;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public void assignVersion(long version) {
        this.version = version;
    }

    /**
     * @return the card drawn.
     */
    public PlayerCard cardDrawn() {
        return cardDrawn;
    }

    @Override
    public String toString() {
        return "PlayerDrawPileCardDrawnEvent[" + drawPileId +
                ", v" + version +
                ", " + cardDrawn +
                "]";
    }
}
