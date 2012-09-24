package arollavengers.core.exceptions.pandemic;

import arollavengers.core.domain.user.User;
import javax.annotation.Nonnull;

public class InvalidUserException extends PandemicRuntimeException {

    public InvalidUserException(@Nonnull final User user) {
        super("User invalid " + user);
    }
}
