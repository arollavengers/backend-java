package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.PlayerCard;
import arollavengers.core.infrastructure.Id;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PlayerDrawPileCardDrawnEvent implements PlayerDrawPileEvent {
    private final Id drawPileId;
    private final PlayerCard drawnCard;
    private long version;

    public PlayerDrawPileCardDrawnEvent(Id drawPileId, PlayerCard drawnCard) {
        this.drawPileId = drawPileId;
        this.drawnCard = drawnCard;
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
        return drawnCard;
    }

    @Override
    public String toString() {
        return "PlayerDrawPileCardDrawnEvent[" + drawPileId +
                ", v" + version +
                ", " + drawnCard +
                "]";
    }
}
