package arollavengers.core.domain.user;

import static org.fest.assertions.api.Assertions.assertThat;

import arollavengers.core.events.user.UserCreatedEvent;
import arollavengers.core.infrastructure.Bus;
import arollavengers.core.infrastructure.DomainEvent;
import arollavengers.core.infrastructure.DummyUnitOfWork;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.SimpleBus;
import arollavengers.core.usecase.CollectorListener;

import org.junit.Before;
import org.junit.Test;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class UserTest {

    private DummyUnitOfWork uow;
    private Bus bus;
    private CollectorListener collectorListener;

    @Before
    public void setUp() {
        collectorListener = new CollectorListener();
        bus = new SimpleBus();
        bus.subscribe(collectorListener);

        uow = new DummyUnitOfWork(bus);
    }

    @Test
    public void create_then_password_is_digested() {
        Id userId = Id.next();

        User user = new User(userId, uow);
        user.createUser("Travis", "Pacman".toCharArray(), "hop".getBytes());

        List<DomainEvent> allUncommitted = uow.getAllUncommitted();

        assertThat(allUncommitted).hasSize(1);
        assertThat(allUncommitted.get(0)).isInstanceOf(UserCreatedEvent.class);
        UserCreatedEvent createdEvent = (UserCreatedEvent) allUncommitted.get(0);
        assertThat(createdEvent.login()).isEqualTo("Travis");
        assertThat(createdEvent.passwordDigest()).isNotEqualTo("Pacman".getBytes());
    }

    @Test
    public void checkPassword() {
        Id userId = Id.next();

        User user = new User(userId, uow);
        user.createUser("Travis", "Pacman".toCharArray(), "hop".getBytes());
        assertThat(user.checkPassword("Mccallum".toCharArray())).isFalse();
        assertThat(user.checkPassword("Pacman".toCharArray())).isTrue();
    }
}
