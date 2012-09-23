package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.Member;
import arollavengers.core.domain.pandemic.PlayerCard;
import arollavengers.core.infrastructure.Id;

public class PlayerDrawnCardFromPileEvent implements WorldEvent {

    private final Id worldId;
    private final Member member;
    private final PlayerCard playerCard;
    private long version;

    public PlayerDrawnCardFromPileEvent(final Id worldId, final Member member, PlayerCard playerCard) {
        this.worldId = worldId;
        this.member = member;
        this.playerCard = playerCard;
    }

    public Member member() {
        return member;
    }

    public PlayerCard playerCard() {
        return playerCard;
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
    public void assignVersion(final long newVersion) {
        this.version = newVersion;
    }

    @Override
    public String toString() {
        return "PlayerDrawnCardFromPileEvent[" + worldId +
                ", v" + version +
                ", " + member +
                ", " + playerCard +
                "]";
    }
}
