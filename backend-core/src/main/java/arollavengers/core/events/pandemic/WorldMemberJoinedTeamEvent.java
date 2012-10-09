package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.MemberRole;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

public class WorldMemberJoinedTeamEvent implements PandemicEvent {

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

    /**
     * @see #worldId()
     */
    @Override
    public Id entityId() {
        return worldId;
    }

    public Id worldId() {
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
        return "WorldMemberJoinedTeamEvent[" + worldId
                + ", " + memberId
                + ", " + newComerId + ": " + role + "]";
    }

}
