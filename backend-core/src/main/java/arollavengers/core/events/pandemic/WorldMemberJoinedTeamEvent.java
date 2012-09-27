package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.MemberRole;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class")
public class WorldMemberJoinedTeamEvent implements WorldEvent {

    @JsonProperty
    private long version;

    @JsonProperty
    private final Id worldId;

    @JsonProperty
    private final Id memberId;

    @JsonProperty
    private final Id newComerId;

    @JsonProperty
    private final MemberRole role;

    @JsonCreator
    public WorldMemberJoinedTeamEvent(@JsonProperty("worldId") Id worldId,
                                      @JsonProperty("memberId") Id memberId,
                                      @JsonProperty("newComerId") Id newComerId,
                                      @JsonProperty("role") MemberRole role) {
        this.worldId = worldId;
        this.memberId = memberId;
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

    public Id memberId() {
        return memberId;
    }

    public Id newComerId() {
        return newComerId;
    }

    public MemberRole role() {
        return role;
    }

    @Override
    public String toString() {
        return "WorldMemberJoinedTeamEvent[" + worldId + ", v" + version + ", " + memberId + ", " + newComerId + ": " + role + "]";
    }

}
