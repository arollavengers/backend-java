package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.PlayerCard;
import arollavengers.core.infrastructure.Id;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PlayerDrawPilePileDrawnEvent implements PlayerDrawPileEvent {
    private final Id playerDrawCardId;
    private final PlayerCard drawnCard;
    private long version;

    public PlayerDrawPilePileDrawnEvent(Id playerDrawCardId, PlayerCard drawnCard) {
        this.playerDrawCardId = playerDrawCardId;
        this.drawnCard = drawnCard;
    }

    @Override
    public Id entityId() {
        return playerDrawCardId;
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
        return "PlayerDrawPilePileDrawnEvent[" + playerDrawCardId +
                ", v" + version +
                ", " + drawnCard +
                "]";
    }
}
