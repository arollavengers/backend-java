package arollavengers.core.usecase;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import arollavengers.core.TestSettings;
import arollavengers.core.domain.pandemic.Difficulty;
import arollavengers.core.domain.pandemic.MemberKey;
import arollavengers.core.domain.pandemic.MemberRole;
import arollavengers.core.domain.pandemic.World;
import arollavengers.core.domain.pandemic.WorldRepositorySupport;
import arollavengers.core.domain.user.UserLoginIndexSupport;
import arollavengers.core.domain.user.UserRepositorySupport;
import arollavengers.core.exceptions.user.UserNotFoundException;
import arollavengers.core.infrastructure.EventStore;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.SimpleBus;
import arollavengers.core.infrastructure.UnitOfWork;
import arollavengers.core.infrastructure.UnitOfWorkDefault;
import arollavengers.core.infrastructure.UnitOfWorkFactory;
import arollavengers.core.service.pandemic.WorldService;
import arollavengers.core.service.user.UserService;
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

        Id userId = Id.next();
        givenAFreshNewUserWithId(userId);

        // When
        Id worldId = Id.next();
        worldService.createWorld(worldId, userId, Difficulty.Normal);

        // Then
        eventStore.dump(System.out);
    }

    @Test(expected = UserNotFoundException.class)
    public void create_world_with_an_unknown_user() throws Exception {
        // Given
        prepareEnvironment();

        Id userId = Id.next();

        // When
        Id worldId = Id.next();
        worldService.createWorld(worldId, userId, Difficulty.Normal);

        // Then
        fail("An exception should have been raised");
    }

    @Test
    public void join_a_game_with_the_owner() throws Exception {
        // Given
        prepareEnvironment();

        Id ownerId = Id.next();
        givenAFreshNewUserWithId(ownerId);
        Id worldId = Id.next();
        givenAFreshNewWorldWithIdAndOwner(worldId, ownerId);

        // When
        worldService.joinGame(worldId, new MemberKey(ownerId, MemberRole.Medic));

        // Then
        eventStore.dump(System.out);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void start_a_game() throws Exception {
        // Given
        prepareEnvironment();

        Id ownerId = Id.next();
        Id playerId1 = Id.next();
        Id playerId2 = Id.next();
        givenAFreshNewUserWithId(ownerId, "Travis");
        givenAFreshNewUserWithId(playerId1, "Vlad");
        givenAFreshNewUserWithId(playerId2, "Thundercat");

        Id worldId = Id.next();
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
        }

        {
            UnitOfWork uow = unitOfWorkFactory.create();
            World world = worldRepository.getWorld(uow, worldId);
            assertThat(world).isNotNull();

            world.startPlayerTurn();
            uow.commit();
        }

        // When
        collectorListener.dump(System.out);
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
