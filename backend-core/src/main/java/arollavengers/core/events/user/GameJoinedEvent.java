package arollavengers.core.events.user;

import arollavengers.core.infrastructure.Id;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class GameJoinedEvent implements UserEvent {

    private long version;
    private final Id userId;
    private final Id gameId;

    public GameJoinedEvent(Id userId, Id gameId) {
        this.userId = userId;
        this.gameId = gameId;
    }

    public long version() {
        return version;
    }

    public void assignVersion(final long version) {
        this.version = version;
    }

    public Id aggregateId() {
        return userId;
    }

    public Id gameId() {
        return gameId;
    }
}
