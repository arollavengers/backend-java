package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.PlayerCard;
import arollavengers.core.infrastructure.Id;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PlayerDrawPileCompletedForDifficultyEvent implements PlayerDrawPileEvent {
    private final Id drawPileId;
    private final List<PlayerCard> cards;
    private long version;

    public PlayerDrawPileCompletedForDifficultyEvent(Id drawPileId, List<PlayerCard> cards) {
        this.drawPileId = drawPileId;
        this.cards = Lists.newArrayList(cards);
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
    public List<PlayerCard> cards() {
        return Lists.newArrayList(cards);
    }

    @Override
    public String toString() {
        return "PlayerDrawPileInitializedEvent[" + drawPileId +
                ", v" + version +
                ", " + cards +
                "]";
    }
}
