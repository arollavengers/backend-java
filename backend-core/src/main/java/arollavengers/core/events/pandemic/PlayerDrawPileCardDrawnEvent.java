package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.PlayerCard;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PlayerDrawPileCardDrawnEvent implements PlayerDrawPileEvent {

    @JsonProperty
    private final Id drawPileId;

    @JsonProperty
    private final PlayerCard cardDrawn;

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

    /**
     * @return the card drawn.
     */
    public PlayerCard cardDrawn() {
        return cardDrawn;
    }

    @Override
    public String toString() {
        return "PlayerDrawPileCardDrawnEvent[" + drawPileId +
                ", " + cardDrawn +
                "]";
    }
}
