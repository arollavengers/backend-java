package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.MemberKey;
import arollavengers.core.infrastructure.Id;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CurrentPlayerDefinedEvent implements MemberEvent {

    private long version;
    private final Id worldId;
    private final MemberKey memberKey;

    public CurrentPlayerDefinedEvent(Id worldId, MemberKey memberKey) {
        this.worldId = worldId;
        this.memberKey = memberKey;
    }

    @Override
    public Id entityId() {
        return worldId;
    }

    public MemberKey memberKey() {
        return memberKey;
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
        return "CurrentPlayerDefinedEvent[" + worldId +
                ", v" + version +
                ", " + memberKey +
                "]";
    }
}
