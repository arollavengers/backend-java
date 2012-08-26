package arollavengers.core.exceptions;

public class WorldNotYetCreatedException extends RuntimeException {
  public WorldNotYetCreatedException() {
    super("World not yet created");
  }
}
