package arollavengers.core.exceptions.eventstore;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class EventStoreRuntimeException extends RuntimeException {
  public EventStoreRuntimeException() {
    super();
  }

  public EventStoreRuntimeException(String message) {
    super(message);
  }

  public EventStoreRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public EventStoreRuntimeException(Throwable cause) {
    super(cause);
  }
}
