package arollavengers.core.events.pandemic;

import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PlayerTurnStartedEvent implements MemberEvent {

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

    public int nbActions () {
        return nbActions;
    }

    @Override
    public String toString() {
        return "PlayerTurnStartedEvent[" + memberId +
                ", nbActions: " + nbActions +
                "]";
    }
}
