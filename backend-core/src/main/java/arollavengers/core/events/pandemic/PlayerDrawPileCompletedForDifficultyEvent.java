package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.PlayerCard;
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
public class PlayerDrawPileCompletedForDifficultyEvent implements PlayerDrawPileEvent {

    @JsonProperty
    private final Id drawPileId;

    @JsonProperty
    private final PlayerCard[] cards;

    @JsonCreator
    public PlayerDrawPileCompletedForDifficultyEvent(@JsonProperty("drawPileId") Id drawPileId,
                                                     @JsonProperty("cards") PlayerCard[] cards)
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
    public List<PlayerCard> cards() {
        return Lists.newArrayList(cards);
    }

    @Override
    public String toString() {
        return "PlayerDrawPileInitializedEvent[" + drawPileId +
                ", " + Arrays.toString(cards) +
                "]";
    }
}
