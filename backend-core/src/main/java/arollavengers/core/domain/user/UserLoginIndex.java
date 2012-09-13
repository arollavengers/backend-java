package arollavengers.core.domain.user;

import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.UnitOfWork;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface UserLoginIndex {
  /**
   *
   * @param uow
   * @param login
   * @throws arollavengers.core.exceptions.user.LoginAlreadyInUseException
   */
  void useLogin(UnitOfWork uow, String login, Id userId);

  /**
   *
   * @param uow
   * @param login
   * @param userId
   */
  void unuseLogin(UnitOfWork uow, String login, Id userId);
}
