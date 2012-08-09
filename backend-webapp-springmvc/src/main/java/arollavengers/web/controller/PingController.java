package arollavengers.web.controller;

import arollavengers.core.service.time.TimeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Controller
@RequestMapping("/ping")
public class PingController {

    @Autowired
    private TimeService timeService;

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public
    @ResponseBody
    Pong ping(@PathVariable String name) {
        return new Pong(name, timeService.currentTimeMillis());
    }

    public static class Pong {
        public final String name;
        public final long timeMillis;

        public Pong(String name, long timeMillis) {
            this.name = name;
            this.timeMillis = timeMillis;
        }
    }

}
