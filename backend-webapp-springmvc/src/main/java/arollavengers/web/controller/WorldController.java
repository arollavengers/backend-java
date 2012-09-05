package arollavengers.web.controller;

import arollavengers.core.domain.pandemic.Difficulty;
import arollavengers.core.domain.user.User;
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

  @RequestMapping("/create")
  public Id create(UserId ownerId, Difficulty difficulty) {

    Assert.notNull(ownerId, "Owner is mandatory");
    Assert.notNull(difficulty, "difficulty is mandatory");

    //TODO make this controller better
    return worldService.createNew(User.withId(ownerId), difficulty);
  }
}