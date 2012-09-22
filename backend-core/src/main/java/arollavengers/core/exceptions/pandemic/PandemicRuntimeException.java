package arollavengers.core.exceptions.pandemic;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PandemicRuntimeException extends RuntimeException {
    public PandemicRuntimeException() {
        super();
    }

    public PandemicRuntimeException(String message) {
        super(message);
    }

    public PandemicRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PandemicRuntimeException(Throwable cause) {
        super(cause);
    }
}
