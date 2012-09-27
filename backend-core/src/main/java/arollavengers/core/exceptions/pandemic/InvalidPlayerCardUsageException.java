package arollavengers.core.exceptions.pandemic;

import arollavengers.core.domain.pandemic.PlayerCard;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class InvalidPlayerCardUsageException extends PandemicRuntimeException {
    public InvalidPlayerCardUsageException(PlayerCard playerCard) {
        super("Card " + playerCard + " usage invalid");
    }

    public InvalidPlayerCardUsageException(String message) {
        super(message);
    }
}
