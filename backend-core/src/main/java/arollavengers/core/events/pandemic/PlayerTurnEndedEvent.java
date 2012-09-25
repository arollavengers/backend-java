package arollavengers.core.events.pandemic;

import arollavengers.core.infrastructure.Id;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PlayerTurnEndedEvent implements MemberEvent {

    private long version;
    private final Id memberId;

    public PlayerTurnEndedEvent(Id memberId) {
        this.memberId = memberId;
    }

    @Override
    public Id entityId() {
        return memberId;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public void assignVersion(long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "PlayerTurnEndedEvent[" + memberId +
                ", v" + version +
                "]";
    }
}
