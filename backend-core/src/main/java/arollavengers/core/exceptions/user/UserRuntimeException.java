package arollavengers.core.exceptions.user;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class UserRuntimeException extends RuntimeException {
  public UserRuntimeException() {
    super();
  }

  public UserRuntimeException(String message) {
    super(message);
  }

  public UserRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public UserRuntimeException(Throwable cause) {
    super(cause);
  }
}
