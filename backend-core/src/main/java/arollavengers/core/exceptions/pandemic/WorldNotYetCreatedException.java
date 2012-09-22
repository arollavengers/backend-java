package arollavengers.core.exceptions.pandemic;

public class WorldNotYetCreatedException extends PandemicRuntimeException {
  public WorldNotYetCreatedException() {
    super("World not yet created");
  }
}
