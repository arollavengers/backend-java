package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.InfectionCard;
import arollavengers.core.infrastructure.Id;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class InfectionDrawPileInitializedEvent implements PlayerDrawPileEvent {
    private final Id drawPileId;
    private final List<InfectionCard> cards;
    private long version;

    public InfectionDrawPileInitializedEvent(Id drawPileId, List<InfectionCard> cards) {
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
    public List<InfectionCard> cards() {
        return Lists.newArrayList(cards);
    }

    @Override
    public String toString() {
        return "InfectionDrawPileInitializedEvent[" + entityId() +
                ", v" + version +
                ", " + cards +
                "]";
    }
}
