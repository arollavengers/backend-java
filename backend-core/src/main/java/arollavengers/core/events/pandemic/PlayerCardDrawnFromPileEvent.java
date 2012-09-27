package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.PlayerCard;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class")
public class PlayerCardDrawnFromPileEvent implements WorldEvent {

    @JsonProperty
    private final Id memberId;

    @JsonProperty
    private final PlayerCard card;

    @JsonProperty
    private long version;

    @JsonCreator
    public PlayerCardDrawnFromPileEvent(@JsonProperty("memberId") Id memberId,
                                        @JsonProperty("card") PlayerCard card) {
        this.memberId = memberId;
        this.card = card;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public Id entityId() {
        return memberId;
    }

    @Override
    public void assignVersion(final long newVersion) {
        this.version = newVersion;
    }

    public PlayerCard playerCard() {
        return card;
    }

    @Override
    public String toString() {
        return "PlayerCardDrawnFromPileEvent[" + memberId +
                ", v" + version +
                ", " + card +
                "]";
    }
}
