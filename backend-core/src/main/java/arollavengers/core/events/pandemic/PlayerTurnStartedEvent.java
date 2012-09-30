package arollavengers.core.events.pandemic;

import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PlayerTurnStartedEvent implements MemberEvent {

    @JsonProperty
    private long version;

    @JsonProperty
    private final Id memberId;

    @JsonProperty
    private final int nbActions;

    @JsonCreator
    public PlayerTurnStartedEvent(@JsonProperty("memberId") Id memberId,
                                  @JsonProperty("nbActions") int nbActions) {
        this.memberId = memberId;
        this.nbActions = nbActions;
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

    public int nbActions () {
        return nbActions;
    }

    @Override
    public String toString() {
        return "PlayerTurnStartedEvent[" + memberId +
                ", v" + version +
                ", nbActions: " + nbActions +
                "]";
    }
}
