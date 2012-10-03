package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.MemberKey;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CurrentPlayerDefinedEvent implements MemberEvent {

    @JsonProperty
    private final Id worldId;

    @JsonProperty
    private final MemberKey memberKey;

    @JsonCreator
    public CurrentPlayerDefinedEvent(@JsonProperty("worldId") Id worldId,
                                     @JsonProperty("memberKey") MemberKey memberKey)
    {
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
    public String toString() {
        return "CurrentPlayerDefinedEvent[" + worldId +
                ", " + memberKey +
                "]";
    }
}
