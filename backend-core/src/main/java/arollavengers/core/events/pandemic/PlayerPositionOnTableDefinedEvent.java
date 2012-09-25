package arollavengers.core.events.pandemic;

import arollavengers.core.infrastructure.Id;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PlayerPositionOnTableDefinedEvent implements MemberEvent {

    private final Id memberId;
    private final int positionOnTable;
    private long version;

    public PlayerPositionOnTableDefinedEvent(Id memberId, int positionOnTable) {
        this.memberId = memberId;
        this.positionOnTable = positionOnTable;
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

    public int positionOnTable() {
        return positionOnTable;
    }

    @Override
    public String toString() {
        return "PlayerPositionOnTableDefinedEvent[" + entityId() +
                ", v" + version +
                ", positionOnTable: " + positionOnTable +
                "]";
    }
}
