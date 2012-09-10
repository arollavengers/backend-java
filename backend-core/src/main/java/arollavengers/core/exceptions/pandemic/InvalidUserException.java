package arollavengers.core.exceptions.pandemic;

import arollavengers.core.domain.user.User;
import org.jetbrains.annotations.NotNull;

public class InvalidUserException extends RuntimeException {

    public InvalidUserException(@NotNull final User user) {
        super("User invalid " + user);
    }
}
