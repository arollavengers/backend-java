package arollavengers.core.domain.pandemic;

import arollavengers.core.domain.user.User;
import arollavengers.core.exceptions.EntityIdAlreadyAssignedException;
import arollavengers.core.exceptions.pandemic.*;
import arollavengers.core.infrastructure.DummyUnitOfWork;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.UnitOfWork;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

public class WorldTest {

    private World w;
    private DummyUnitOfWork uow;
    private User ownr;
    private User user;
    private User othr;

    @Before
    public void newWorld() {
        uow = new DummyUnitOfWork();
        w = new World(uow);

        ownr = createUser(uow, "Travis");
        user = createUser(uow, "Jamiro");
        othr = createUser(uow, "Pacman");
        uow.clearUncommitted();
    }

    @Test
    public void defaults() {
        assertThat(w.aggregateId()).isEqualTo(Id.undefined());
    }

    @Test
    public void testCreateWorld() {

        //Given -- a new instance of a world
        //See @Before

        //When -- A world is created
        final Id worldId = Id.next();
        w.createWorld(worldId, ownr, Difficulty.Heroic);

        //Then --
        assertThat(w.aggregateId()).isEqualTo(worldId);
        assertThat(w.difficulty()).isEqualTo(Difficulty.Heroic);
        assertThat(w.ownerId()).isEqualTo(ownr.aggregateId());
        assertThat(w.rolesAssigned()).isEmpty();
        for (Disease disease : Disease.values()) {
            assertThat(w.hasBeenEradicated(disease)).isFalse();
            assertThat(w.hasCureFor(disease)).isFalse();
        }

        // Then -- cards are not initialized yet
        assertThat(w.playerDrawCardsSize()).isEqualTo(0);
        //      -- No city has a research center
        assertThat(w.citiesWithResearchCenters()).hasSize(0);
    }

    @Test(expected = EntityIdAlreadyAssignedException.class)
    public void testCreateWorld_When_AlreadyCreated() {
        //Given -- A world is created
        final Id worldId = Id.next();
        w.createWorld(worldId, ownr, Difficulty.Heroic);

        //When -- the world is REcreated
        try {
            w.createWorld(worldId, ownr, Difficulty.Heroic);
        }

        //Then -- An exception is thrown
        catch (EntityIdAlreadyAssignedException e) {
            assertThat(e).isEqualTo(new EntityIdAlreadyAssignedException(worldId, worldId));
            throw e;
        }
        fail("EntityIdAlreadyAssignedException should be catched and throwed");
    }

    @Test
    public void testRegisterRole() {
        //Given -- a new instance of a world
        final Id worldId = Id.next();
        w.createWorld(worldId, ownr, Difficulty.Heroic);

        //When -- a player join the game
        w.registerMember(user, MemberRole.Medic);

        //Then -- the role is registered
        assertThat(w.isRoleAssigned(MemberRole.Medic)).isTrue();

    }

    @Test(expected = WorldNotYetCreatedException.class)
    public void testRegisterRole_WhenGameNotYetCreated() {
        //Given -- a new instance of a world
        //See @Before
        assertThat(w.isStarted()).isFalse();

        //When -- a player join the game
        w.registerMember(user, MemberRole.Dispatcher);
    }

    @Test(expected = WorldRoleAlreadyChosenException.class)
    public void testRegisterRole_WhenRoleAlreadyRegistered() {
        //Given -- a new instance of a world
        final Id worldId = Id.next();
        w.createWorld(worldId, ownr, Difficulty.Heroic);
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
        w.createWorld(worldId, ownr, Difficulty.Heroic);
        // with a medic already registerd
        w.registerMember(user, MemberRole.Medic);

        //When -- the same user try to register
        try {
            w.registerMember(user, MemberRole.Dispatcher);
        }

        //Then -- an exception is thrown
        catch (UserAlreadyRegisteredException e) {
            assertThat(e).isEqualTo(new UserAlreadyRegisteredException(worldId, user.aggregateId()));
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
        w.createWorld(worldId, ownr, Difficulty.Heroic);
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
        w.createWorld(worldId, ownr, Difficulty.Heroic);

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
        //See @Before


        //When -- the game is started but not yet created
        w.startGame();

    }

    @Test(expected = NotEnoughPlayerException.class)
    public void startGame_WhenNotEnoughMember() {

        //Given -- a world created
        final Id worldId = Id.next();
        w.createWorld(worldId, ownr, Difficulty.Heroic);

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
        w.createWorld(worldId, ownr, Difficulty.Heroic);

        //Given -- there are 2 players registered
        w.registerMember(ownr, MemberRole.Dispatcher);
        w.registerMember(user, MemberRole.Medic);


        //When -- game is started
        w.startGame();


        //Then -- game is started (no kidding)
        assertThat(w.isStarted()).isTrue();

        //Then -- each players has 4 PlayerCard
        final int initialCardsPerMember = 4;
        assertThat(w.memberHandSize(ownr.aggregateId()).get()).isEqualTo(initialCardsPerMember);
        assertThat(w.memberHandSize(user.aggregateId()).get()).isEqualTo(initialCardsPerMember);

        //Then -- the player draw cards is initialized
        final int remainingDrawPlayerCards = CityId.values().length - (initialCardsPerMember * 2);
        assertThat(w.playerDrawCardsSize()).isEqualTo(remainingDrawPlayerCards);

        //Then -- Atlanta is the only city with a research center
        assertThat(w.citiesWithResearchCenters()).hasSize(1);
        assertThat(w.citiesWithResearchCenters().iterator().next()).isEqualTo(CityId.Atlanta);

    }

    private static User createUser(UnitOfWork uow, String login) {
        User user = new User(uow);
        user.createUser(Id.next(), login, "password".toCharArray(), "salt".toCharArray());
        return user;
    }
}