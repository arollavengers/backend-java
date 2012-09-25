package arollavengers.core.events.pandemic;

import arollavengers.core.infrastructure.Id;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class InfectionDrawPileCreatedEvent implements WorldEvent {
    private final Id worldId;
    private final Id drawPileId;
    private long version;

    public InfectionDrawPileCreatedEvent(final Id worldId, final Id drawPileId) {
        this.worldId = worldId;
        this.drawPileId = drawPileId;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public Id entityId() {
        return worldId;
    }

    @Override
    public void assignVersion(final long version) {
        this.version = version;
    }

    public Id drawPileId() {
        return drawPileId;
    }

    @Override
    public String toString() {
        return "InfectionDrawPileCreatedEvent[" + worldId +
                ", v" + version +
                ", " + drawPileId +
                "]";
    }
}
