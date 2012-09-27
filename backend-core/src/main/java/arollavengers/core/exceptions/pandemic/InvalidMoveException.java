package arollavengers.core.exceptions.pandemic;

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.domain.pandemic.MemberKey;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class InvalidMoveException extends PandemicRuntimeException {
    public InvalidMoveException(MemberKey memberKey, CityId location, CityId destination) {
        super("Player (" + memberKey + ") cannot be moved from " + location + " to " + destination);
    }
}
