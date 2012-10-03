package arollavengers.core.events.user;

import arollavengers.core.domain.GameType;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class GameJoinedEvent implements UserEvent {

    @JsonProperty
    private final Id userId;

    @JsonProperty
    private final Id gameId;

    @JsonProperty
    private final GameType gameType;

    @JsonCreator
    public GameJoinedEvent(@JsonProperty("userId") Id userId,
                           @JsonProperty("gameId") Id gameId,
                           @JsonProperty("gameType") GameType gameType) {
        this.userId = userId;
        this.gameId = gameId;
        this.gameType = gameType;
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
        return "GameJoinedEvent[" + userId
                + ", " + gameType + "/" + gameId + "]";
    }
}
