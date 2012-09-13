package arollavengers.core.exceptions.user;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class LoginAlreadyInUseException extends UserRuntimeException {
  public LoginAlreadyInUseException(String message) {
    super(message);
  }
}
