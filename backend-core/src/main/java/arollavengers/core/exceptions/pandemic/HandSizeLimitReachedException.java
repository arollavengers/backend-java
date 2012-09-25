package arollavengers.core.exceptions.pandemic;

import arollavengers.core.infrastructure.Id;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class HandSizeLimitReachedException extends PandemicRuntimeException {
    public HandSizeLimitReachedException(Id memberId, int handSize) {
        super("Member <" + memberId + "> has already #" + handSize + " cards in his hand");
    }
}
