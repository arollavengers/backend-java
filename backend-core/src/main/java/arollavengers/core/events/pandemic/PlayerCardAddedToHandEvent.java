package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.PlayerCard;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

public class PlayerCardAddedToHandEvent implements PandemicEvent {

    @JsonProperty
    private final Id memberId;

    @JsonProperty
    private final PlayerCard card;

    @JsonCreator
    public PlayerCardAddedToHandEvent(@JsonProperty("memberId") Id memberId,
                                      @JsonProperty("card") PlayerCard card) {
        this.memberId = memberId;
        this.card = card;
    }

    @Override
    public Id entityId() {
        return memberId;
    }

    public PlayerCard playerCard() {
        return card;
    }

    @Override
    public String toString() {
        return "PlayerCardAddedToHandEvent[" + memberId +
                ", " + card +
                "]";
    }
}
