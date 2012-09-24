package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.MemberRole;
import arollavengers.core.infrastructure.Id;

public class WorldMemberJoinedTeamEvent implements WorldEvent {

    private long version;
    private final Id worldId;
    private final Id newComerId;
    private final MemberRole role;

    public WorldMemberJoinedTeamEvent(final Id worldId, final Id newComerId, final MemberRole role) {
        this.worldId = worldId;
        this.newComerId = newComerId;
        this.role = role;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public void assignVersion(final long version) {
        this.version = version;
    }

    @Override
    public Id entityId() {
        return worldId;
    }

    public Id newComerId() {
        return newComerId;
    }

    public MemberRole role() {
        return role;
    }

    @Override
    public String toString() {
        return "WorldMemberJoinedTeamEvent[" + worldId + ", v" + version + ", " + newComerId + ": " + role + "]";
    }

}
