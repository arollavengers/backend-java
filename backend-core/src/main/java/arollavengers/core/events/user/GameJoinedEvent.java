package arollavengers.core.events.user;

import arollavengers.core.domain.GameType;
import arollavengers.core.infrastructure.Id;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class GameJoinedEvent implements UserEvent {

    private long version;
    private final Id userId;
    private final Id gameId;
    private final GameType gameType;

    public GameJoinedEvent(Id userId, Id gameId, GameType gameType) {
        this.userId = userId;
        this.gameId = gameId;
        this.gameType = gameType;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public void assignVersion(final long version) {
        this.version = version;
    }

    @Override
    public Id entityId() {
        return userId;
    }

    public Id gameId() {
        return gameId;
    }

    public GameType gameType() {
        return gameType;
    }

    @Override
    public String toString() {
        return "GameJoinedEvent[" + userId + ", v" + version + ", " + gameId + "]";
    }
}
