package arollavengers.core.events.pandemic;

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
public class PlayerTurnEndedEvent implements MemberEvent {

    @JsonProperty
    private long version;

    @JsonProperty
    private final Id memberId;

    @JsonCreator
    public PlayerTurnEndedEvent(@JsonProperty("memberId") Id memberId) {
        this.memberId = memberId;
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
        return "PlayerTurnEndedEvent[" + memberId +
                ", v" + version +
                "]";
    }
}
