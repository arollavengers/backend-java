package arollavengers.core.infrastructure;

import static org.fest.assertions.api.Assertions.assertThat;

import arollavengers.core.domain.user.User;
import arollavengers.core.events.user.UserCreatedEvent;
import arollavengers.pattern.annotation.CanBeInvokedOnlyOnce;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class DomainEventTest {

    @Test
    public void assignVersion_canBeInvokedAtLeastOnce() {
        UserCreatedEvent anEvent = new UserCreatedEvent(Id.next(User.class), "Travis", "Pacman".getBytes(), "hop".getBytes());
        anEvent.assignVersion(17L);

        assertThat(anEvent.version()).isEqualTo(17L);
    }

    @Test(expected = CanBeInvokedOnlyOnce.MethodAlreadyInvokedException.class)
    public void assignVersion_canOnlyBeInvokedOnce() {
        UserCreatedEvent anEvent = new UserCreatedEvent(Id.next(User.class), "Travis", "Pacman".getBytes(), "hop".getBytes());
        anEvent.assignVersion(17L);
        anEvent.assignVersion(23L);
        Assert.fail("Cannot invoke assignVersion twice!");
    }
}
