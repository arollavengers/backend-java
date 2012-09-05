package arollavengers.core.service.pandemic;

import arollavengers.core.domain.pandemic.Difficulty;
import arollavengers.core.domain.user.User;
import arollavengers.core.domain.pandemic.World;
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
