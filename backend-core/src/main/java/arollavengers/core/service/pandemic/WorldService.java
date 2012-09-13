package arollavengers.core.service.pandemic;

import arollavengers.core.domain.pandemic.Difficulty;
import arollavengers.core.domain.pandemic.WorldRepository;
import arollavengers.core.domain.user.User;
import arollavengers.core.domain.pandemic.World;
import arollavengers.core.domain.user.UserRepository;
import arollavengers.core.exceptions.user.UserNotFoundException;
import arollavengers.core.infrastructure.EventStore;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.UnitOfWork;
import arollavengers.core.infrastructure.UnitOfWorkFactory;

import org.springframework.stereotype.Service;
import javax.inject.Inject;

@Service
public class WorldService {

  @Inject
  private UnitOfWorkFactory unitOfWorkFactory;

  @Inject
  private UserRepository userRepository;

  @Inject
  private WorldRepository worldRepository;

  public void createWorld(Id worldId, Id ownerId, Difficulty difficulty) {
    UnitOfWork uow = unitOfWorkFactory.create();

    User user = userRepository.getUser(uow, ownerId);
    if(user==null) {
      throw new UserNotFoundException("Id: " + ownerId);
    }

    World world = new World(uow);
    world.createWorld(worldId, user, difficulty);
    worldRepository.add(uow, world);
    uow.commit();
  }

  /**
   * Define the {@link UnitOfWorkFactory} used by the service.
   *
   * @param unitOfWorkFactory
   */
  public void setUnitOfWorkFactory(UnitOfWorkFactory unitOfWorkFactory) {
    this.unitOfWorkFactory = unitOfWorkFactory;
  }
}
