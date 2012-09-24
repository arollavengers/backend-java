package arollavengers.core.events.user;

import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class")
public class UserCreatedEvent implements UserEvent {

    @JsonProperty
    private long version;

    @JsonProperty
    private final Id newUserId;

    @JsonProperty
    private final String login;

    @JsonProperty
    private final byte[] passwordDigest;

    @JsonProperty
    private final byte[] salt;

    @JsonCreator
    public UserCreatedEvent(@JsonProperty("newUserId") Id newUserId,
                            @JsonProperty("login") String login,
                            @JsonProperty("passwordDigest") byte[] passwordDigest,
                            @JsonProperty("salt") byte[] salt)
    {
        this.newUserId = newUserId;
        this.login = login;
        this.passwordDigest = passwordDigest;
        this.salt = salt;
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
        return newUserId;
    }

    public String login() {
        return login;
    }

    public byte[] passwordDigest() {
        return passwordDigest;
    }

    public byte[] salt() {
        return salt;
    }

    @Override
    public String toString() {
        return "UserCreatedEvent[" + newUserId + ", v" + version + ", " + login + "]";
    }
}
