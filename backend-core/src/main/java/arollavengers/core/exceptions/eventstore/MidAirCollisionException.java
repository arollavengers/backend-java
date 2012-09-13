package arollavengers.core.exceptions.eventstore;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class MidAirCollisionException extends EventStoreRuntimeException {
  public MidAirCollisionException(String message) {
    super(message);
  }
}
