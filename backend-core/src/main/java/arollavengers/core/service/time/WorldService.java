package arollavengers.core.service.time;

import arollavengers.core.domain.Difficulty;
import arollavengers.core.domain.User;
import arollavengers.core.domain.World;
import arollavengers.core.events.DummyUnitOfWork;
import arollavengers.core.infrastructure.Id;
import org.springframework.stereotype.Service;

@Service
public class WorldService {

  public Id create(final Difficulty difficulty){
    final World world = new World(new DummyUnitOfWork());
    final Id newId = Id.next();
    world.createWorld(newId,new User(), difficulty);
    return newId;
  }
}
