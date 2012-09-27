package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.InfectionCard;
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
public class InfectionDrawPileCardDrawnEvent implements PlayerDrawPileEvent {
    @JsonProperty
    private final Id drawPileId;

    @JsonProperty
    private final InfectionCard cardDrawn;

    @JsonProperty
    private long version;

    @JsonCreator
    public InfectionDrawPileCardDrawnEvent(@JsonProperty("drawPileId") Id drawPileId,
                                           @JsonProperty("cardDrawn") InfectionCard cardDrawn)
    {
        this.drawPileId = drawPileId;
        this.cardDrawn = cardDrawn;
    }

    @Override
    public Id entityId() {
        return drawPileId;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public void assignVersion(long version) {
        this.version = version;
    }

    /**
     * @return the card drawn.
     */
    public InfectionCard cardDrawn() {
        return cardDrawn;
    }

    @Override
    public String toString() {
        return "PlayerDrawPileCardDrawnEvent[" + drawPileId +
                ", v" + version +
                ", " + cardDrawn +
                "]";
    }
}
