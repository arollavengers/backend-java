package arollavengers.core.domain.user;

import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.UnitOfWork;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface UserRepository {
  void addUser(@Nonnull UnitOfWork uow, @Nonnull User user);

  @Nullable
  User getUser(@Nonnull UnitOfWork uow, @Nonnull Id userId);
}
