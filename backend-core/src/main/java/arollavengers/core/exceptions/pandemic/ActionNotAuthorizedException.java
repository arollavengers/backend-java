package arollavengers.core.exceptions.pandemic;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ActionNotAuthorizedException extends PandemicRuntimeException {
    public ActionNotAuthorizedException(int nbActionRemaining) {
        super("Number of action remaining: " + nbActionRemaining);
    }
}
