package arollavengers.core.exceptions;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class InfrastructureRuntimeException extends RuntimeException {
    public InfrastructureRuntimeException() {
    }

    public InfrastructureRuntimeException(String message) {
        super(message);
    }

    public InfrastructureRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InfrastructureRuntimeException(Throwable cause) {
        super(cause);
    }
}
