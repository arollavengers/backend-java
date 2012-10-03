package arollavengers.core.events.user;

import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
* @author <a href="http://twitter.com/aloyer">@aloyer</a>
*/
public class LoginRemovedEvent extends LoginEvent {

    @JsonProperty
    private final String login;

    @JsonCreator
    public LoginRemovedEvent(@JsonProperty("indexId") Id indexId, @JsonProperty("login") String login) {
        super(indexId);
        this.login = login;
    }

    public String login() {
        return login;
    }

    @Override
    public String toString() {
        return "LoginRemovedEvent[" + entityId() + ", " + login() + "]";
    }
}
