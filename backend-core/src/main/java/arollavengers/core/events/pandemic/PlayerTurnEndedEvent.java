package arollavengers.core.events.pandemic;

import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PlayerTurnEndedEvent implements MemberEvent {

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
    public String toString() {
        return "PlayerTurnEndedEvent[" + memberId +
                "]";
    }
}
