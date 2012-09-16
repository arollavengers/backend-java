package arollavengers.core.events.user;

import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class UserCreatedEvent implements UserEvent {

  @JsonProperty
  private long version;

  @JsonProperty
  private final Id newUserId;

  @JsonProperty
  private final String login;

  @JsonProperty
  private final char[] passwordDigest;

  @JsonProperty
  private final char[] salt;

  @JsonCreator
  public UserCreatedEvent(@JsonProperty("newUserId") Id newUserId,
                          @JsonProperty("login") String login,
                          @JsonProperty("passwordDigest") char[] passwordDigest,
                          @JsonProperty("salt") char[] salt) {
    this.newUserId = newUserId;
    this.login = login;
    this.passwordDigest = passwordDigest;
    this.salt = salt;
  }

  public long version() {
    return version;
  }

  public void assignVersion(final long version) {
    this.version = version;
  }

  public Id aggregateId() {
    return newUserId;
  }

  public String login() {
    return login;
  }

  public char[] passwordDigest() {
    return passwordDigest;
  }

  public char[] salt() {
    return salt;
  }

  @Override
  public String toString() {
    return "UserCreatedEvent[" + newUserId + ", v" + version + ", " + login + "]";
  }
}
