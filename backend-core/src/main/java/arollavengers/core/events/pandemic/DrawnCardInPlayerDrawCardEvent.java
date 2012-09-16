package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.Member;
import arollavengers.core.infrastructure.Id;

public class DrawnCardInPlayerDrawCardEvent implements WorldEvent {

    private final Id worldId;

    private final Member member;

    private long version;

    public DrawnCardInPlayerDrawCardEvent(final Id worldId, final Member member) {
        this.worldId = worldId;
        this.member = member;
    }

    public Member member() {
        return member;
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
