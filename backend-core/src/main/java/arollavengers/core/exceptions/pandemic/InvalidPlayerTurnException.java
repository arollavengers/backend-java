package arollavengers.core.exceptions.pandemic;

import arollavengers.core.domain.pandemic.MemberKey;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class InvalidPlayerTurnException extends PandemicRuntimeException {
    public InvalidPlayerTurnException(MemberKey currentMemberKey, MemberKey memberKey) {
        super("Invalid player turn for " + memberKey + " expecting " + currentMemberKey);
    }
}
