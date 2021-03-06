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
public class PlayerDrawPileInitializedEvent implements PlayerDrawPileEvent {

    @JsonProperty
    private final Id playerDrawPileId;

    @JsonProperty
    private final PlayerCard[] cards;

    @JsonCreator
    public PlayerDrawPileInitializedEvent(@JsonProperty("playerDrawPileId") Id playerDrawPileId,
                                          @JsonProperty("cards") PlayerCard[] cards) {
        this.playerDrawPileId = playerDrawPileId;
        this.cards = cards;
    }

    @Override
    public Id entityId() {
        return playerDrawPileId;
    }

    /**
     * @return an copy of the cards.
     */
    public List<PlayerCard> cards() {
        return Lists.newArrayList(cards);
    }

    @Override
    public String toString() {
        return "PlayerDrawPileInitializedEvent[" + playerDrawPileId +
                ", cards=" + Arrays.toString(cards) +
                "]";
    }
}
