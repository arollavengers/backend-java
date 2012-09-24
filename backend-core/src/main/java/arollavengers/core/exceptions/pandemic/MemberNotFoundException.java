package arollavengers.core.exceptions.pandemic;

import arollavengers.core.domain.pandemic.MemberKey;
import arollavengers.core.exceptions.InfrastructureRuntimeException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class MemberNotFoundException extends InfrastructureRuntimeException {
    public MemberNotFoundException(MemberKey memberKey) {
        super("" + memberKey.userId() + "/" + memberKey.role());
    }
}
