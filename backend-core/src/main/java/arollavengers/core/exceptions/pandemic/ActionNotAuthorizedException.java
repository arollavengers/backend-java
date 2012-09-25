package arollavengers.core.exceptions.pandemic;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ActionNotAuthorizedException extends PandemicRuntimeException {
    public ActionNotAuthorizedException(boolean currentPlayer, int nbActionRemaining) {
        super("Is current player? " + currentPlayer + ", number of action remaining: " + nbActionRemaining);
    }
}
