package arollavengers.web.controller;

import arollavengers.core.domain.pandemic.Difficulty;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.service.pandemic.WorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/world")
public class WorldController {

    @Autowired
    WorldService worldService;

<<<<<<< HEAD
  @RequestMapping("/create")
  public Id create(Id ownerId, Difficulty difficulty) {
=======
    @RequestMapping("/create")
    public Id create(Id ownerId, Difficulty difficulty) {
>>>>>>> 9fa960759af9ba826f0bab5eb102e1bf87268e38

        Assert.notNull(ownerId, "Owner is mandatory");
        Assert.notNull(difficulty, "difficulty is mandatory");

<<<<<<< HEAD
    Id worldId = Id.next();
    worldService.createWorld(worldId, ownerId, difficulty);
    return worldId;
  }
=======
        //TODO make this controller better
        // return worldService.createNew(User.withId(ownerId), difficulty);
        return null;
    }
>>>>>>> 9fa960759af9ba826f0bab5eb102e1bf87268e38
}
