package arollavengers.core.events.pandemic;

import arollavengers.core.infrastructure.Id;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CurrentPlayerDefinedEvent implements MemberEvent {

    private long version;
    private final Id memberId;
    private final int nbActions;

    public CurrentPlayerDefinedEvent(Id memberId, int nbActions) {
        this.memberId = memberId;
        this.nbActions = nbActions;
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

    public int nbActions () {
        return nbActions;
    }

    @Override
    public String toString() {
        return "CurrentPlayerDefinedEvent[" + memberId +
                ", v" + version +
                "]";
    }
}
