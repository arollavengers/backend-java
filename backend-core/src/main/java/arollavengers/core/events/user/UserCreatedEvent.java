package arollavengers.core.events.user;

import arollavengers.core.infrastructure.Id;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class UserCreatedEvent implements UserEvent {

  private long version;
  private final Id newUserId;
  private final String login;
  private final char[] passwordDigest;
  private final char[] salt;

  public UserCreatedEvent(Id newUserId, String login, char[] passwordDigest, char[] salt) {
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
