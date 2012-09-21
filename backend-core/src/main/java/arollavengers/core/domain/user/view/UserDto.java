package arollavengers.core.domain.user.view;

import arollavengers.core.domain.user.UserDetails;
import arollavengers.core.infrastructure.Id;
import arollavengers.pattern.annotation.ValueObject;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@ValueObject
public class UserDto {
  private final Id userId;
  private final String login;
  private final UserDetails details;

  public UserDto(Id userId, String login, UserDetails details) {
    this.userId = userId;
    this.login = login;
    this.details = details;
  }

  public Id getUserId() {
    return userId;
  }

  public String getLogin() {
    return login;
  }

  public UserDetails getDetails() {
    return details;
  }
}
