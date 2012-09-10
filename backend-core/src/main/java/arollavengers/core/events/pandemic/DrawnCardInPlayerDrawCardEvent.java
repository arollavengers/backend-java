package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.MemberRole;
import arollavengers.core.infrastructure.Id;

public class DrawnCardInPlayerDrawCardEvent implements WorldEvent {

    private final Id worldId;

    private final MemberRole memberRole;

    private long version;

    public DrawnCardInPlayerDrawCardEvent(final Id worldId, final MemberRole memberRole) {
        this.worldId = worldId;
        this.memberRole = memberRole;
    }

    public MemberRole memberRole() {
        return memberRole;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public Id aggregateId() {
        return worldId;
    }

    @Override
    public void assignVersion(final long newVersion) {
        this.version = newVersion;
    }
}
