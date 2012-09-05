package arollavengers.core.service.world;

import arollavengers.core.domain.Difficulty;
import arollavengers.core.domain.User;
import arollavengers.core.domain.World;
import arollavengers.core.infrastructure.Id;
import org.springframework.stereotype.Service;

@Service
public class WorldService {
  public Id createNew(User owner, Difficulty difficulty) {
    final Id newId = Id.next();
    new World(null).createWorld(newId,owner,difficulty);
    return newId;
  }
}
