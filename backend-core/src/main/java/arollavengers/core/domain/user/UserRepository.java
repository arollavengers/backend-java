package arollavengers.core.domain.user;

import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.UnitOfWork;

import org.jetbrains.annotations.Nullable;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface UserRepository {

  /**
   * Return the user (if it exists) with the specified id.
   *
   * @param uow unit of work in which the loaded user will be attached
   * @param userId the user's id
   * @return
   */
  @Nullable
  User getUser(UnitOfWork uow, Id userId);
}
