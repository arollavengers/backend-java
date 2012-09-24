package arollavengers.core.domain.pandemic;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

import arollavengers.core.domain.user.User;
import arollavengers.core.infrastructure.UnitOfWorkDefault;
import arollavengers.core.exceptions.EntityAlreadyCreatedException;
import arollavengers.core.exceptions.pandemic.GameAlreadyStartedException;
import arollavengers.core.exceptions.pandemic.NotEnoughPlayerException;
import arollavengers.core.exceptions.pandemic.UserAlreadyRegisteredException;
import arollavengers.core.exceptions.pandemic.WorldNotYetCreatedException;
import arollavengers.core.exceptions.pandemic.WorldNumberOfRoleLimitReachedException;
import arollavengers.core.exceptions.pandemic.WorldRoleAlreadyChosenException;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.SimpleBus;
import arollavengers.core.infrastructure.UnitOfWork;
import org.junit.Before;
import org.junit.Test;

public class WorldTest {

    private World w;
    private UnitOfWorkDefault uow;
    private User ownr;
    private User user;
    private User othr;

    @Before
    public void newWorld() {
        SimpleBus bus = new SimpleBus();
        uow = new UnitOfWorkDefault(bus);

        ownr = createUser(uow, "Travis");
        user = createUser(uow, "Jamiro");
        othr = createUser(uow, "Pacman");
        uow.clearUncommitted();
    }

    @Test
    public void testCreateWorld() {

        //Given -- a new instance of a world
        //See @Before

        //When -- A world is created
        final Id worldId = Id.next();
        w = new World(worldId, uow);
        w.createWorld(ownr, Difficulty.Heroic);

        //Then --
        assertThat(w.entityId()).isEqualTo(worldId);
        assertThat(w.difficulty()).isEqualTo(Difficulty.Heroic);
        assertThat(w.ownerId()).isEqualTo(ownr.entityId());
        assertThat(w.rolesAssigned()).isEmpty();
        for (Disease disease : Disease.values()) {
            assertThat(w.hasBeenEradicated(disease)).isFalse();
            assertThat(w.hasCureFor(disease)).isFalse();
        }

        // Then -- cards are not initialized yet
        assertThat(w.playerDrawPileSize()).isEqualTo(0);
        //      -- No city has a research center
        assertThat(w.citiesWithResearchCenters()).hasSize(0);
    }

    @Test(expected = EntityAlreadyCreatedException.class)
    public void testCreateWorld_When_AlreadyCreated() {
        //Given -- A world is created
        final Id worldId = Id.next();
        w = new World(worldId, uow);
        w.createWorld(ownr, Difficulty.Heroic);

        //When -- the world is REcreated
        try {
            w.createWorld(ownr, Difficulty.Heroic);
        }

        //Then -- An exception is thrown
        catch (EntityAlreadyCreatedException e) {
            assertThat(e.getEntityId()).isEqualTo(worldId);
            throw e;
        }
        fail("EntityAlreadyCreatedException should be catched and throwed");
    }

    @Test
    public void testRegisterRole() {
        //Given -- a new instance of a world
        final Id worldId = Id.next();
        w = new World(worldId, uow);
        w.createWorld(ownr, Difficulty.Heroic);

        //When -- a player join the game
        w.registerMember(user, MemberRole.Medic);

        //Then -- the role is registered
        assertThat(w.isRoleAssigned(MemberRole.Medic)).isTrue();

    }

    @Test(expected = WorldNotYetCreatedException.class)
    public void testRegisterRole_WhenGameNotYetCreated() {
        //Given -- a new instance of a world
        //See @Before
        final Id worldId = Id.next();
        w = new World(worldId, uow);
        assertThat(w.isStarted()).isFalse();

        //When -- a player join the game
        w.registerMember(user, MemberRole.Dispatcher);
    }

    @Test(expected = WorldRoleAlreadyChosenException.class)
    public void testRegisterRole_WhenRoleAlreadyRegistered() {
        //Given -- a new instance of a world
        final Id worldId = Id.next();
        w = new World(worldId, uow);
        w.createWorld(ownr, Difficulty.Heroic);
        // with a medic already registerd
        w.registerMember(user, MemberRole.Medic);

        //When -- another medic try to register
        try {
            w.registerMember(othr, MemberRole.Medic);
        }

        //Then -- an exception is thrown
        catch (WorldRoleAlreadyChosenException e) {
            assertThat(e).isEqualTo(new WorldRoleAlreadyChosenException(worldId, MemberRole.Medic));
            throw e;
        }
        fail("WorldRoleAlreadyChosenException should be catched and throwed");
    }

    @Test(expected = UserAlreadyRegisteredException.class)
    public void testRegisterRole_WhenPlayerAlreadyRegistered_withADifferentRole() {
        //Given -- a new instance of a world
        final Id worldId = Id.next();
        w = new World(worldId, uow);
        w.createWorld(ownr, Difficulty.Heroic);
        // with a medic already registerd
        w.registerMember(user, MemberRole.Medic);

        //When -- the same user try to register
        try {
            w.registerMember(user, MemberRole.Dispatcher);
        }

        //Then -- an exception is thrown
        catch (UserAlreadyRegisteredException e) {
            assertThat(e).isEqualTo(new UserAlreadyRegisteredException(worldId, user.entityId()));
            throw e;
        }
        fail("WorldRoleAlreadyChosenException should be catched and throwed");
    }

    @Test(expected = WorldNumberOfRoleLimitReachedException.class)
    public void testRegisterRole_WhenPlayerLimitIsReached() {
        //Given -- a new instance of a world
        User user1 = createUser(uow, "Jill");
        User user2 = createUser(uow, "Jule");
        User user3 = createUser(uow, "Jack");
        User user4 = createUser(uow, "John");
        uow.clearUncommitted();

        final Id worldId = Id.next();
        w = new World(worldId, uow);
        w.createWorld(ownr, Difficulty.Heroic);
        // with 4 roles registered
        w.registerMember(user1, MemberRole.Medic);
        w.registerMember(user2, MemberRole.Dispatcher);
        w.registerMember(user3, MemberRole.OperationsExpert);
        w.registerMember(user4, MemberRole.Researcher);

        //When -- another member try to register
        try {
            w.registerMember(othr, MemberRole.Scientist);
        }

        //Then -- an exception is thrown
        catch (WorldNumberOfRoleLimitReachedException e) {
            assertThat(e).isEqualTo(new WorldNumberOfRoleLimitReachedException(MemberRole.Scientist));
            throw e;
        }
        fail("WorldNumberOfRoleLimitReachedException should be catched and throwed");

    }

    @Test(expected = GameAlreadyStartedException.class)
    public void testRegisterRole_WhenGameIsAlreadyStarted() {
        //Given -- a world created
        final Id worldId = Id.next();
        w = new World(worldId, uow);
        w.createWorld(ownr, Difficulty.Heroic);

        //Given -- there are 2 players registered
        w.registerMember(ownr, MemberRole.Dispatcher);
        w.registerMember(user, MemberRole.Medic);

        //Given -- the game is started
        w.startGame();


        //When -- another user try to register
        w.registerMember(othr, MemberRole.Scientist);

        //Then -- an exception is thrown
    }

    @Test(expected = WorldNotYetCreatedException.class)
    public void startGame_WhenGameNotCreated() {

        //Given -- a new instance of a world
        final Id worldId = Id.next();
        w = new World(worldId, uow);

        //When -- the game is started but not yet created
        w.startGame();

    }

    @Test(expected = NotEnoughPlayerException.class)
    public void startGame_WhenNotEnoughMember() {

        //Given -- a world created
        final Id worldId = Id.next();
        w = new World(worldId, uow);
        w.createWorld(ownr, Difficulty.Heroic);

        //Given -- there is only one player registered
        w.registerMember(ownr, MemberRole.Researcher);

        //When -- game is started
        try {
            w.startGame();
        }

        //Then -- an exception is thrown
        catch (NotEnoughPlayerException e) {
            assertThat(e).isEqualTo(new NotEnoughPlayerException(1, 2));
            throw e;
        }
        fail("NotEnoughPlayerException should be catched and throwed");
    }

    @Test
    public void startGame_With2Players() {
        //Given -- a world created
        final Id worldId = Id.next();
        w = new World(worldId, uow);
        w.createWorld(ownr, Difficulty.Heroic);

        //Given -- there are 2 players registered
        w.registerMember(ownr, MemberRole.Dispatcher);
        w.registerMember(user, MemberRole.Medic);


        //When -- game is started
        w.startGame();


        //Then -- game is started (no kidding)
        assertThat(w.isStarted()).isTrue();

        //Then -- each players has 4 PlayerCard
        final int initialCardsPerMember = 4;
        MemberKey ownrKey = new MemberKey(ownr.entityId(), MemberRole.Dispatcher);
        MemberKey userKey = new MemberKey(user.entityId(), MemberRole.Medic);
        assertThat(w.memberHandSize(ownrKey)).isEqualTo(initialCardsPerMember);
        assertThat(w.memberHandSize(userKey)).isEqualTo(initialCardsPerMember);

        //Then -- the player draw cards is initialized
// TODO       final int remainingDrawPlayerCards = CityId.values().length - (initialCardsPerMember * 2);
// TODO       assertThat(w.playerDrawPileSize()).isEqualTo(remainingDrawPlayerCards);

        //Then -- Atlanta is the only city with a research center
        assertThat(w.citiesWithResearchCenters()).hasSize(1);
        assertThat(w.citiesWithResearchCenters()).contains(CityId.Atlanta);

        //Then -- Infection rate is 2
        assertThat(w.infectionRate()).isEqualTo(2);

        //Then -- Number of outbreaks
        assertThat(w.outbreaks()).isEqualTo(0);

    }

    private static User createUser(UnitOfWork uow, String login) {
        User user = new User(Id.next(), uow);
        user.createUser(login, "password".toCharArray(), "salt".getBytes());
        return user;
    }
}