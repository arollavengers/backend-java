package arollavengers.core.events.pandemic;

import arollavengers.core.infrastructure.Id;

public class GameStartedEvent implements WorldEvent {

    private long version;

    private final Id worldId;

    public GameStartedEvent(final Id worldId) {
        this.worldId = worldId;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public Id aggregateId() {
        return worldId;
    }

    @Override
    public void assignVersion(final long version) {
        this.version = version;
    }
}
