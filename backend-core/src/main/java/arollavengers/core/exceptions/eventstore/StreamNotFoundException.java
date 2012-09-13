package arollavengers.core.exceptions.eventstore;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class StreamNotFoundException extends EventStoreRuntimeException {
  public StreamNotFoundException(String message) {
    super(message);
  }
}
