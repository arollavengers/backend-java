package arollavengers.web.controller.pandemic;

import arollavengers.core.domain.pandemic.Difficulty;
import arollavengers.core.domain.pandemic.World;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.service.pandemic.WorldService;
import arollavengers.core.views.pandemic.CityView;
import arollavengers.core.views.pandemic.CityViewRepository;
import arollavengers.web.dto.ResultDto;

import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.inject.Inject;

@Controller
@RequestMapping("/pandemic")
public class PandemicController {

    @Inject
    private WorldService worldService;

    @Inject
    private CityViewRepository cityViewRepository;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResultDto create(Id ownerId, Difficulty difficulty) {

        Assert.notNull(ownerId, "Owner is mandatory");
        Assert.notNull(difficulty, "difficulty is mandatory");

        Id worldId = Id.next(World.class);
        worldService.createWorld(worldId, ownerId, difficulty);
        return ResultDto.ok(worldId);
    }

    @RequestMapping(value = "/cities", method = RequestMethod.GET)
    public ResultDto cityViews(WorldQueryDto query) {
        CityView[] cityViews = cityViewRepository.listAllInWorld(query.worldId);
        return ResultDto.ok(cityViews);
    }

    public static class WorldQueryDto {
        @JsonProperty
        public Id worldId;
    }
}
