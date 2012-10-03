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
public class IntensityOfInfectionIncreasedEvent implements PlayerDrawPileEvent {

    @JsonProperty
    private final Id drawPileId;

    @JsonProperty
    private final InfectionCard[] cards;

    @JsonCreator
    public IntensityOfInfectionIncreasedEvent(@JsonProperty("drawPileId") Id drawPileId,
                                              @JsonProperty("cards") InfectionCard[] cards)
    {
        this.drawPileId = drawPileId;
        this.cards = cards;
    }

    @Override
    public Id entityId() {
        return drawPileId;
    }

    /**
     * @return an copy of the cards.
     */
    public List<InfectionCard> cards() {
        return Lists.newArrayList(cards);
    }

    @Override
    public String toString() {
        return "IntensityOfInfectionIncreasedEvent[" + entityId() +
                ", " + Arrays.toString(cards) +
                "]";
    }
}
