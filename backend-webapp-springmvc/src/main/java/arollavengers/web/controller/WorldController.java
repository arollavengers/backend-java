package arollavengers.web.controller;

import arollavengers.core.domain.pandemic.Difficulty;
import arollavengers.core.domain.pandemic.World;
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
    public Id create(Id ownerId, Difficulty difficulty) {

        Assert.notNull(ownerId, "Owner is mandatory");
        Assert.notNull(difficulty, "difficulty is mandatory");

        Id worldId = Id.next(World.class);
        worldService.createWorld(worldId, ownerId, difficulty);
        return worldId;
    }
}
