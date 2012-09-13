package arollavengers.core.domain.user;

import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.UnitOfWork;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface UserRepository {
  void addUser(@NotNull UnitOfWork uow, @NotNull User user);

  @Nullable
  User getUser(@NotNull UnitOfWork uow, @NotNull Id userId);
}
