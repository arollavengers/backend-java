package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.InfectionCard;
import arollavengers.core.infrastructure.Id;
import com.google.common.collect.Lists;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class")
public class InfectionDrawPileInitializedEvent implements PlayerDrawPileEvent {
    @JsonProperty
    private final Id drawPileId;

    @JsonProperty
    private final InfectionCard[] cards;

    @JsonProperty
    private long version;

    @JsonCreator
    public InfectionDrawPileInitializedEvent(@JsonProperty("drawPileId") Id drawPileId,
                                             @JsonProperty("cards") InfectionCard[] cards)
    {
        this.drawPileId = drawPileId;
        this.cards = cards;
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
     * @return an copy of the cards.
     */
    public List<InfectionCard> cards() {
        return Lists.newArrayList(cards);
    }

    @Override
    public String toString() {
        return "InfectionDrawPileInitializedEvent[" + entityId() +
                ", v" + version +
                ", " + Arrays.toString(cards) +
                "]";
    }
}
