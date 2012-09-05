package arollavengers.core.service.user;

import arollavengers.core.domain.user.User;
import arollavengers.core.infrastructure.Id;

public interface UserService {
  //TODO This has to be in factory or in service ?
  User byId(Id userId);
}
