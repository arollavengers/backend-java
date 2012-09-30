package arollavengers.core.events.user;

import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
* @author <a href="http://twitter.com/aloyer">@aloyer</a>
*/
public class LoginIndexCreatedEvent extends LoginEvent {

    @JsonCreator
    public LoginIndexCreatedEvent(@JsonProperty("indexId") Id indexId) {
        super(indexId);
    }

    @Override
    public String toString() {
        return "LoginIndexCreatedEvent[" + entityId() + ", v" + version() + "]";
    }
}
