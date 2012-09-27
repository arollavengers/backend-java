package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.MemberKey;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class")
public class FirstPlayerDesignatedEvent implements MemberEvent {

    @JsonProperty
    private long version;

    @JsonProperty
    private final Id worldId;

    @JsonProperty
    private final MemberKey memberKey;

    @JsonCreator
    public FirstPlayerDesignatedEvent(@JsonProperty("worldId") Id worldId,
                                      @JsonProperty("memberKey") MemberKey memberKey)
    {
        this.worldId = worldId;
        this.memberKey = memberKey;
    }

    public MemberKey memberKey() {
        return memberKey;
    }

    @Override
    public Id entityId() {
        return worldId;
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
        return "FirstPlayerDesignatedEvent[" + entityId() +
                ", v" + version +
                ", " + memberKey +
                "]";
    }
}
