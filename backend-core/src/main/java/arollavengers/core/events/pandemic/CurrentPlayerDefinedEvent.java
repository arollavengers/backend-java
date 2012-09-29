package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.MemberKey;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CurrentPlayerDefinedEvent implements MemberEvent {

    @JsonProperty
    private long version;

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
