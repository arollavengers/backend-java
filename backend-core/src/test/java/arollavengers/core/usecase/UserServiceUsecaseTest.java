package arollavengers.core.usecase;

import static org.fest.assertions.api.Assertions.assertThat;

import arollavengers.core.TestSettings;
import arollavengers.core.domain.user.User;
import arollavengers.core.domain.user.UserLoginIndexSupport;
import arollavengers.core.domain.user.UserRepositorySupport;
import arollavengers.core.exceptions.user.LoginAlreadyInUseException;
import arollavengers.core.infrastructure.UnitOfWorkDefault;
import arollavengers.core.infrastructure.EventStore;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.Message;
import arollavengers.core.infrastructure.SimpleBus;
import arollavengers.core.infrastructure.UnitOfWork;
import arollavengers.core.infrastructure.UnitOfWorkFactory;
import arollavengers.core.service.user.UserService;
import arollavengers.core.testutils.TypeOfEventStore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@SuppressWarnings("ConstantConditions")
public class UserServiceUsecaseTest {

    private final TypeOfEventStore typeOfEventStore;
    //
    private TestSettings testSettings;
    //
    private UserService userService;
    private UserRepositorySupport userRepository;
    private UnitOfWorkFactory unitOfWorkFactory;
    private SimpleBus bus;
    private CollectorListener collectorListener;

    public UserServiceUsecaseTest() {
        this(TypeOfEventStore.InMemory);
    }

    protected UserServiceUsecaseTest(TypeOfEventStore typeOfEventStore) {
        this.typeOfEventStore = typeOfEventStore;
    }

    @Before
    public void setUp() throws IOException {
        testSettings = new TestSettings();
    }

    @Test
    public void createUser_then_commit_and_getIt() throws Exception {
        // Given
        prepareEnvironment();

        // When
        UnitOfWork uow = unitOfWorkFactory.create();
        Id newUserId = Id.next(User.class);
        userService.createUser(uow, newUserId, "Travis", "Pacman".toCharArray());
        uow.commit();

        // ~~~
        // eventStore.dump(System.out);
        // dumpMessages();

        // Then
        uow = unitOfWorkFactory.create();
        User user = userRepository.getUser(uow, newUserId);
        assertThat(user).isNotNull();
        assertThat(user.login()).isEqualTo("Travis");

    }

    @Test
    public void createUser_and_getUser_within_the_same_unitOfWork() throws Exception {
        // Given
        prepareEnvironment();

        // When
        UnitOfWork uow = unitOfWorkFactory.create();
        Id newUserId = Id.next(User.class);
        User userCreated = userService.createUser(uow, newUserId, "Travis", "Pacman".toCharArray());

        // Then
        User user = userRepository.getUser(uow, newUserId);
        assertThat(user).isNotNull();
        assertThat(user).isSameAs(userCreated);
    }

    @Test
    public void getUser_returns_always_the_same_instance_within_the_same_unitOfWork() throws Exception {
        // Given
        prepareEnvironment();

        // When
        Id newUserId = Id.next(User.class);
        UnitOfWork uow = unitOfWorkFactory.create();
        userService.createUser(uow, newUserId, "Travis", "Pacman".toCharArray());
        uow.commit();

        // Then
        uow = unitOfWorkFactory.create();
        User user1 = userRepository.getUser(uow, newUserId);
        assertThat(user1).isNotNull();
        assertThat(user1.login()).isEqualTo("Travis");

        User user2 = userRepository.getUser(uow, newUserId);
        assertThat(user2).isSameAs(user1);
    }

    @Test
    public void create_user_with_login_already_used() throws Exception {
        // Given
        prepareEnvironment();

        // When
        Id newUserId = Id.next(User.class);
        UnitOfWork uow = unitOfWorkFactory.create();
        userService.createUser(uow, newUserId, "Travis", "Pacman".toCharArray());
        uow.commit();

        collectorListener.clearMessages();

        // Then
        Id newUserId2 = Id.next(User.class);
        uow = unitOfWorkFactory.create();
        try {
            userService.createUser(uow, newUserId2, "Travis", "Wahouu".toCharArray());
            Assert.fail("An exception should have been raised");
        }
        catch (LoginAlreadyInUseException alreadyInUse) {
            // great!
        }
        assertThat(collectorListener.getMessages()).isEmpty();
    }

    @Test
    public void login_with_valid_password () throws Exception {
        // Given
        prepareEnvironment();

        Id newUserId = Id.next(User.class);
        UnitOfWork uow = unitOfWorkFactory.create();
        userService.createUser(uow, newUserId, "Travis", "Pacman".toCharArray());
        uow.commit();

        // When
        Id loggedId = userService.getUserWithCredentials("Travis", "Pacman".toCharArray());

        // Then
        assertThat(loggedId).isEqualTo(newUserId);
    }

    @Test
    public void login_with_unknown_login () throws Exception {
        // Given
        prepareEnvironment();

        // When
        Id loggedId = userService.getUserWithCredentials("Travis", "Pacman".toCharArray());

        // Then
        assertThat(loggedId).isNull();
    }

    @Test
    public void login_with_invalid_password () throws Exception {
        // Given
        prepareEnvironment();

        Id newUserId = Id.next(User.class);
        UnitOfWork uow = unitOfWorkFactory.create();
        userService.createUser(uow, newUserId, "Travis", "Pacman".toCharArray());
        uow.commit();

        // When
        Id loggedId = userService.getUserWithCredentials("Travis", "P4cm4n".toCharArray());

        // Then
        assertThat(loggedId).isNull();
    }

    @SuppressWarnings("UnusedDeclaration")
    private void dumpMessages () {
        System.out.println("UserServiceUsecaseTest.dumpMessages:");
        for (Message message : collectorListener.getMessages()) {
            System.out.println(">> " + message);
        }
    }

    @SuppressWarnings("unchecked")
    private void prepareEnvironment() throws Exception {
        EventStore eventStore = typeOfEventStore.eventStore(testSettings);
        collectorListener = new CollectorListener();
        bus = new SimpleBus();
        bus.subscribe(collectorListener);

        unitOfWorkFactory = new UnitOfWorkFactory() {
            @Override
            public UnitOfWork create() {
                return new UnitOfWorkDefault(bus);
            }
        };

        UserLoginIndexSupport userLoginIndex = new UserLoginIndexSupport();
        userLoginIndex.setEventStore(eventStore);

        userRepository = new UserRepositorySupport();
        userRepository.setEventStore(eventStore);

        userService = new UserService();
        userService.setUnitOfWorkFactory(unitOfWorkFactory);
        userService.setUserRepository(userRepository);
        userService.setUserLoginIndex(userLoginIndex);
    }
}
