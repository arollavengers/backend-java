package arollavengers.web.controller;

import arollavengers.core.infrastructure.Id;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/world")
public class WorldController {
  @RequestMapping("/create")
  public Id create(){

  }
}
