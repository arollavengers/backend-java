package arollavengers.core.domain;

import arollavengers.core.events.DummyUnitOfWork;
import arollavengers.core.exceptions.EntityIdAlreadyAssignedException;
import arollavengers.core.exceptions.WorldNotYetCreatedException;
import arollavengers.core.exceptions.WorldNumberOfRoleLimitReachedException;
import arollavengers.core.exceptions.WorldRoleAlreadyChosenException;
import arollavengers.core.infrastructure.Id;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assume.assumeThat;

public class WorldTest {

  private World w;

  @Before
  public void newWorld() {
    w = brandNewWorld();
    assumeThat(w.aggregateId(), equalTo(Id.undefined()));
  }

  private World brandNewWorld() {
    final DummyUnitOfWork uow = new DummyUnitOfWork();
    return new World(uow);
  }

  @Test
  public void testCreateWorld() {

    //Given -- a new instance of a world
    //See @Before

    //When -- A world is created
    final Id worldId = Id.next();
    final User owner = new User();
    w.createWorld(worldId, owner, Difficulty.Heroic);

    //Then --
    assertThat(w.aggregateId()).isEqualTo(worldId);
    assertThat(w.difficulty()).isEqualTo(Difficulty.Heroic);
    assertThat(w.ownedBy()).isEqualTo(owner.userId());
    assertThat(w.team().roles()).isEmpty();
    for (Disease disease : Disease.values()) {
      assertThat(w.hasBeenEradicated(disease)).isFalse();
      assertThat(w.hasCureFor(disease)).isFalse();
    }
  }

  @Test(expected = EntityIdAlreadyAssignedException.class)
  public void testCreateWorld_When_AlreadyCreated() {
    //Given -- A world is created
    final Id worldId = Id.next();
    final User owner = new User();
    w.createWorld(worldId, owner, Difficulty.Heroic);

    //When -- the world is REcreated
    try {
      w.createWorld(worldId, owner, Difficulty.Heroic);
    }

    //Then -- An exception is thrown
    catch (EntityIdAlreadyAssignedException e) {
      assertThat(e).isEqualTo(new EntityIdAlreadyAssignedException(worldId, worldId));
      throw e;
    }
  }

  @Test
  public void testRegisterRole() {
    //Given -- a new instance of a world
    final Id worldId = Id.next();
    final User owner = new User();
    w.createWorld(worldId, owner, Difficulty.Heroic);

    //When -- a player join the game
    w.registerRole(new Member(MemberRole.Medic));

    //Then -- the role is registered
    assertThat(w.team().hasRole(MemberRole.Medic)).isTrue();

  }

  @Test(expected = WorldNotYetCreatedException.class)
  public void testRegisterRole_WhenGameNotYetCreated() {
    //Given -- a new world
    final World newWorld = brandNewWorld();

    //When -- a player join the game
    w.registerRole(new Member(MemberRole.Dispatcher));
  }

  @Test(expected = WorldRoleAlreadyChosenException.class)
  public void testRegisterRole_WhenPlayerAlreadyRegistered() {
    //Given -- a new instance of a world
    final Id worldId = Id.next();
    final User owner = new User();
    w.createWorld(worldId, owner, Difficulty.Heroic);
    // with a medic already registerd
    w.registerRole(new Member(MemberRole.Medic));

    //When -- another medic try to register
    try {
      w.registerRole(new Member(MemberRole.Medic));
    }

    //Then -- an exception is thrown
    catch (WorldRoleAlreadyChosenException e) {
      assertThat(e).isEqualTo(new WorldRoleAlreadyChosenException(worldId, MemberRole.Medic));
      throw e;
    }
  }

  @Test(expected = WorldNumberOfRoleLimitReachedException.class)
  public void testRegisterRole_WhenPlayerLimitIsReached() {
    //Given -- a new instance of a world
    final Id worldId = Id.next();
    final User owner = new User();
    w.createWorld(worldId, owner, Difficulty.Heroic);
    // with 4 roles registered
    w.registerRole(new Member(MemberRole.Medic));
    w.registerRole(new Member(MemberRole.Dispatcher));
    w.registerRole(new Member(MemberRole.OperationsExpert));
    w.registerRole(new Member(MemberRole.Researcher));

    //When -- another member try to register
    try {
      w.registerRole(new Member(MemberRole.Scientist));
    }

    //Then -- an exception is thrown
    catch (WorldNumberOfRoleLimitReachedException e) {
      assertThat(e).isEqualTo(new WorldNumberOfRoleLimitReachedException(MemberRole.Scientist));
      throw e;
    }

  }
}