package arollavengers.pattern;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PatternViolationException extends RuntimeException {
  public PatternViolationException() {
    super();
  }

  public PatternViolationException(String message) {
    super(message);
  }

  public PatternViolationException(String message, Throwable cause) {
    super(message, cause);
  }

  public PatternViolationException(Throwable cause) {
    super(cause);
  }
}
