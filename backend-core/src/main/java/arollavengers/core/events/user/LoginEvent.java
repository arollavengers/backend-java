package arollavengers.core.events.user;

import arollavengers.core.infrastructure.DomainEvent;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonProperty;

/**
* @author <a href="http://twitter.com/aloyer">@aloyer</a>
*/
public abstract class LoginEvent implements DomainEvent {

    @JsonProperty
    private long version;

    @JsonProperty
    private final Id indexId;

    public LoginEvent(Id indexId) {
        if (indexId.isUndefined()) {
            throw new IllegalArgumentException("Id is undefied!");
        }
        this.indexId = indexId;
    }

    public long version() {
        return version;
    }

    public void assignVersion(final long version) {
        this.version = version;
    }

    @Override
    public Id entityId() {
        return indexId;
    }
}
