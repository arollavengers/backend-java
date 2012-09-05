package arollavengers.core.service.user;

import arollavengers.core.domain.User;
import arollavengers.core.domain.UserId;

public class DummyUserService implements UserService{
  public User byId(final UserId userId) {
    return User.withId(userId);
  }
}
