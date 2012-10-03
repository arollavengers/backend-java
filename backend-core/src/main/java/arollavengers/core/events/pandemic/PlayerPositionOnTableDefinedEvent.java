package arollavengers.core.events.pandemic;

import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PlayerPositionOnTableDefinedEvent implements MemberEvent {

    @JsonProperty
    private final Id memberId;

    @JsonProperty
    private final int positionOnTable;

    @JsonCreator
    public PlayerPositionOnTableDefinedEvent(@JsonProperty("memberId") Id memberId,
                                             @JsonProperty("positionOnTable") int positionOnTable) {
        this.memberId = memberId;
        this.positionOnTable = positionOnTable;
    }

    @Override
    public Id entityId() {
        return memberId;
    }

    public int positionOnTable() {
        return positionOnTable;
    }

    @Override
    public String toString() {
        return "PlayerPositionOnTableDefinedEvent[" + entityId() +
                ", positionOnTable: " + positionOnTable +
                "]";
    }
}
