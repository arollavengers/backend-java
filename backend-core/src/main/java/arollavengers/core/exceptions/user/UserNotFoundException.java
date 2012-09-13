package arollavengers.core.exceptions.user;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class UserNotFoundException extends UserRuntimeException {
  public UserNotFoundException(String message) {
    super(message);
  }
}
