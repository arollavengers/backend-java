package arollavengers.core.service.user;

import arollavengers.core.domain.User;
import arollavengers.core.domain.UserId;

public interface UserService {
  //TODO This has to be in factory or in service ?
  User byId(UserId userId);
}
