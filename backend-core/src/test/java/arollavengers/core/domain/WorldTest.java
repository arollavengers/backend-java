package arollavengers.core.domain;

import arollavengers.core.events.DummyUnitOfWork;
import arollavengers.core.infrastructure.Id;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class WorldTest {

  @Test
  public void testCreateWorld_whenAlreadyAssigned() {

    //Given -- a new instance of a world
    final DummyUnitOfWork uow = new DummyUnitOfWork();
    World w = new World(uow);

    //When -- A world is created
    final Id worldId = Id.next();
    final User owner = new User();
    w.createWorld(worldId, owner, Difficulty.Heroic);

    //Then --
    assertThat(w.aggregateId()).isEqualTo(worldId);
    assertThat(w.difficulty()).isEqualTo(Difficulty.Heroic);
    assertThat(w.ownedBy()).isEqualTo(owner.userId());
    for (Disease disease : Disease.values()) {
      assertThat(w.hasBeenEradicated(disease)).isFalse();
      assertThat(w.hasCureFor(disease)).isFalse();
    }
  }
}
