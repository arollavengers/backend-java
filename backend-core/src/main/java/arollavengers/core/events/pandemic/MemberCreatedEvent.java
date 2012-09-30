package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.MemberRole;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class MemberCreatedEvent implements MemberEvent {

    @JsonProperty
    private long version;

    @JsonProperty
    private final Id memberId;

    @JsonProperty
    private final Id userId;

    @JsonProperty
    private final MemberRole role;

    @JsonCreator
    public MemberCreatedEvent(@JsonProperty("memberId") Id memberId,
                              @JsonProperty("userId") Id userId,
                              @JsonProperty("role") MemberRole role)
    {
        this.memberId = memberId;
        this.userId = userId;
        this.role = role;
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

    @Override
    public String toString() {
        return "MemberCreatedEvent[" + memberId +
                ", v" + version +
                ", user=" + userId +
                ", role=" + role +
                "]";
    }
}
