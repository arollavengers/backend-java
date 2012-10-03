package arollavengers.core.events.user;

import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
* @author <a href="http://twitter.com/aloyer">@aloyer</a>
*/
public class LoginAddedEvent extends LoginEvent {

    @JsonProperty
    private final String login;

    @JsonProperty
    private final Id userId;

    @JsonCreator
    public LoginAddedEvent(@JsonProperty("indexId") Id indexId,
                           @JsonProperty("login") String login,
                           @JsonProperty("userId") Id userId)
    {
        super(indexId);
        this.login = login;
        this.userId = userId;
    }

    public String login() {
        return login;
    }

    @Override
    public String toString() {
        return "LoginAddedEvent[" + entityId()
                + ", " + login() + "]";
    }

    public Id userId() {
        return userId;
    }
}
