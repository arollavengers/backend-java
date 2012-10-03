package arollavengers.core.events.user;

import arollavengers.core.infrastructure.DomainEvent;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonProperty;

/**
* @author <a href="http://twitter.com/aloyer">@aloyer</a>
*/
public abstract class LoginEvent implements DomainEvent {

    @JsonProperty
    private final Id indexId;

    public LoginEvent(Id indexId) {
        if (indexId.isUndefined()) {
            throw new IllegalArgumentException("Id is undefied!");
        }
        this.indexId = indexId;
    }

    @Override
    public Id entityId() {
        return indexId;
    }
}
