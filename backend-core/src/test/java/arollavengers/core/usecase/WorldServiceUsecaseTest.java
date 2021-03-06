package arollavengers.core.usecase;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeThat;

import arollavengers.core.TestSettings;
import arollavengers.core.domain.pandemic.Difficulty;
import arollavengers.core.domain.pandemic.MemberKey;
import arollavengers.core.domain.pandemic.MemberRole;
import arollavengers.core.domain.pandemic.World;
import arollavengers.core.domain.pandemic.WorldRepositorySupport;
import arollavengers.core.domain.user.User;
import arollavengers.core.domain.user.UserLoginIndexSupport;
import arollavengers.core.domain.user.UserRepositorySupport;
import arollavengers.core.exceptions.user.UserNotFoundException;
import arollavengers.core.infrastructure.EventStore;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.SimpleBus;
import arollavengers.core.infrastructure.UnitOfWork;
import arollavengers.core.infrastructure.UnitOfWorkDefault;
import arollavengers.core.infrastructure.UnitOfWorkFactory;
import arollavengers.core.infrastructure.eventstore.EventStoreInMemory;
import arollavengers.core.service.pandemic.WorldService;
import arollavengers.core.service.user.UserService;
import arollavengers.core.testutils.EventStoreInMemoryIO;
import arollavengers.core.testutils.TypeOfEventStore;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class WorldServiceUsecaseTest {

    private final TypeOfEventStore typeOfEventStore;
    //
    private TestSettings testSettings;
    //
    private CollectorListener collectorListener;
    private SimpleBus bus;
    private UnitOfWorkFactory unitOfWorkFactory;
    private UserService userService;
    private WorldService worldService;
    private EventStore eventStore;
    private WorldRepositorySupport worldRepository;
    private UserRepositorySupport userRepository;

    public WorldServiceUsecaseTest() {
        this(TypeOfEventStore.InMemory);
    }

    protected WorldServiceUsecaseTest(TypeOfEventStore typeOfEventStore) {
        this.typeOfEventStore = typeOfEventStore;
    }

    @Before
    public void setUp() throws IOException {
        testSettings = new TestSettings();
    }

    @Test
    public void create_world_with_an_existing_user() throws Exception {
        // Given
        prepareEnvironment();

        Id userId = Id.next(User.class);
        givenAFreshNewUserWithId(userId);

        // When
        Id worldId = Id.next(User.class);
        worldService.createWorld(worldId, userId, Difficulty.Normal);

        // Then
        eventStore.dump(System.out);
    }

    @Test(expected = UserNotFoundException.class)
    public void create_world_with_an_unknown_user() throws Exception {
        // Given
        prepareEnvironment();

        Id userId = Id.next(User.class);

        // When
        Id worldId = Id.next(World.class);
        worldService.createWorld(worldId, userId, Difficulty.Normal);

        // Then
        fail("An exception should have been raised (" + userId + ")");
    }

    @Test
    public void join_a_game_with_the_owner() throws Exception {
        // Given
        prepareEnvironment();

        Id ownerId = Id.next(User.class);
        givenAFreshNewUserWithId(ownerId);
        Id worldId = Id.next(User.class);
        givenAFreshNewWorldWithIdAndOwner(worldId, ownerId);

        // When
        worldService.joinGame(worldId, new MemberKey(ownerId, MemberRole.Medic));

        // Then
        eventStore.dump(System.out);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void start_a_game() throws Exception {
        Id.Strategy.TypedSequence.reset();
        Id.setDefaultStrategy(Id.Strategy.TypedSequence);

        // Given
        prepareEnvironment();

        Id ownerId = Id.next(User.class);
        Id playerId1 = Id.next(User.class);
        Id playerId2 = Id.next(User.class);
        givenAFreshNewUserWithId(ownerId, "Travis");
        givenAFreshNewUserWithId(playerId1, "Vlad");
        givenAFreshNewUserWithId(playerId2, "Thundercat");

        Id worldId = Id.next(World.class);
        givenAFreshNewWorldWithIdAndOwner(worldId, ownerId);

        MemberKey memberKey0 = new MemberKey(ownerId, MemberRole.Medic);
        MemberKey memberKey1 = new MemberKey(playerId1, MemberRole.OperationsExpert);
        MemberKey memberKey2 = new MemberKey(playerId2, MemberRole.Dispatcher);
        worldService.joinGame(worldId, memberKey0);
        worldService.joinGame(worldId, memberKey1);
        worldService.joinGame(worldId, memberKey2);

        {
            UnitOfWork uow = unitOfWorkFactory.create();
            World world = worldRepository.getWorld(uow, worldId);
            assertThat(world).isNotNull();

            world.designateFirstPlayer(memberKey1);
            world.startGame();
            uow.commit();

            if (eventStore instanceof EventStoreInMemory) {
                new EventStoreInMemoryIO(testSettings.getProperty("memory.event-store.basedir"))
                        .write((EventStoreInMemory) eventStore);
            }
        }

        {
            UnitOfWork uow = unitOfWorkFactory.create();
            World world = worldRepository.getWorld(uow, worldId);
            assertThat(world).isNotNull();

            world.endCurrentPlayerTurn();
            world.endCurrentPlayerTurn();
            world.endCurrentPlayerTurn();
            world.endCurrentPlayerTurn();
            world.endCurrentPlayerTurn();
            uow.commit();
        }

        // When
        collectorListener.dump(System.out);
    }

    @Test
    public void reload_game001() throws Exception {

        // Given
        prepareEnvironment();
        assumeThat(eventStore, instanceOf(EventStoreInMemory.class));

        new EventStoreInMemoryIO(testSettings.getProperty("memory.event-store.srcdir") + "/game001")
                .load((EventStoreInMemory) eventStore);

        {
            UnitOfWork uow = unitOfWorkFactory.create();
            World world = worldRepository.getWorld(uow, Id.create("World~1"));
            assertThat(world).isNotNull();
            assertThat(world.isStarted()).isTrue();
        }

        eventStore.dump(System.out);
    }

    // ------------------------------------------------------------------------

    private void givenAFreshNewWorldWithIdAndOwner(Id worldId, Id ownerId) {
        worldService.createWorld(worldId, ownerId, Difficulty.Normal);
    }

    private void givenAFreshNewUserWithId(Id userId) {
        givenAFreshNewUserWithId(userId, "Travis");
    }

    private void givenAFreshNewUserWithId(Id userId, String login) {
        UnitOfWork uow = unitOfWorkFactory.create();
        userService.createUser(uow, userId, login, login.toCharArray());
        uow.commit();
    }

    @SuppressWarnings("unchecked")
    private void prepareEnvironment() throws Exception {
        eventStore = typeOfEventStore.eventStore(testSettings);
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

        worldRepository = new WorldRepositorySupport();
        worldRepository.setEventStore(eventStore);

        worldService = new WorldService();
        worldService.setUnitOfWorkFactory(unitOfWorkFactory);
        worldService.setUserRepository(userRepository);
        worldService.setWorldRepository(worldRepository);
    }
}
