package arollavengers.web.controller.pandemic;

import arollavengers.core.domain.pandemic.Difficulty;
import arollavengers.core.domain.pandemic.World;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.service.pandemic.WorldService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/pandemic")
public class PandemicController {

    @Autowired
    WorldService worldService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Id create(Id ownerId, Difficulty difficulty) {

        Assert.notNull(ownerId, "Owner is mandatory");
        Assert.notNull(difficulty, "difficulty is mandatory");

        Id worldId = Id.next(World.class);
        worldService.createWorld(worldId, ownerId, difficulty);
        return worldId;
    }

//    @RequestMapping(value = "/cities", method = RequestMethod.GET)
//    public CityView[] cityViews() {
//
//    }
}
