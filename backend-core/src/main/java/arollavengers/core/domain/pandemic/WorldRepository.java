package arollavengers.core.domain.pandemic;

import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.UnitOfWork;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface WorldRepository {
  void addWorld(@NotNull UnitOfWork uow, @NotNull World world);

  @Nullable
  World getWorld(@NotNull UnitOfWork uow, @NotNull Id worldId);
}
