package arollavengers.core.exceptions.user;

import arollavengers.core.infrastructure.Id;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class UserNotFoundException extends UserRuntimeException {
  public UserNotFoundException(Id userId) {
    super("User with id " + userId + " not found");
  }
}
