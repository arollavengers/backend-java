package arollavengers.core.domain.pandemic;

import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.UnitOfWork;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface WorldRepository {
  void addWorld(@Nonnull UnitOfWork uow, @Nonnull World world);

  @Nullable
  World getWorld(@Nonnull UnitOfWork uow, @Nonnull Id worldId);
}
