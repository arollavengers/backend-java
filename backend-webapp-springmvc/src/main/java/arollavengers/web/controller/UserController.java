package arollavengers.web.controller;

import arollavengers.core.domain.user.User;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.UnitOfWork;
import arollavengers.core.infrastructure.UnitOfWorkFactory;
import arollavengers.core.service.user.UserService;
import arollavengers.web.dto.ResultDto;

import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Controller
@RequestMapping("/users")
public class UserController {

    @Inject
    private UserService userService;

    @Inject
    private UnitOfWorkFactory unitOfWorkFactory;

    @ExceptionHandler(Exception.class)
    public ResultDto handleException(Exception ex, HttpServletRequest request) {
        return ResultDto.err(ex);
    }

    @RequestMapping(method = RequestMethod.POST, consumes="application/json")
    @ResponseBody
    public ResultDto create(@RequestBody LoginDto dto) {
        Assert.notNull(dto.login, "login is mandatory");
        Assert.notNull(dto.password, "password is mandatory");
        Assert.state(dto.password.length > 0, "password is mandatory");

        UnitOfWork uow = unitOfWorkFactory.create();
        User user = userService.createUser(uow, dto.login, dto.password);
        uow.commit();
        return ResultDto.ok(user.entityId());
    }

    public static class LoginDto {
        @JsonProperty
        public String login;
        @JsonProperty
        public char[] password;
    }

}
