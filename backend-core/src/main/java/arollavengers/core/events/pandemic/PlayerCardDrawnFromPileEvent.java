package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.PlayerCard;
import arollavengers.core.infrastructure.Id;

public class PlayerCardDrawnFromPileEvent implements WorldEvent {

    private final Id memberId;
    private final PlayerCard playerCard;
    private long version;

    public PlayerCardDrawnFromPileEvent(final Id memberId, PlayerCard playerCard) {
        this.memberId = memberId;
        this.playerCard = playerCard;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public Id entityId() {
        return memberId;
    }

    @Override
    public void assignVersion(final long newVersion) {
        this.version = newVersion;
    }

    public PlayerCard playerCard() {
        return playerCard;
    }

    @Override
    public String toString() {
        return "PlayerCardDrawnFromPileEvent[" + memberId +
                ", v" + version +
                ", " + playerCard +
                "]";
    }
}
