package arollavengers.core.service.pandemic;

import arollavengers.core.domain.GameType;
import arollavengers.core.domain.pandemic.Difficulty;
import arollavengers.core.domain.pandemic.MemberRole;
import arollavengers.core.domain.pandemic.World;
import arollavengers.core.domain.pandemic.WorldRepository;
import arollavengers.core.domain.user.User;
import arollavengers.core.domain.user.UserRepository;
import arollavengers.core.exceptions.pandemic.WorldNotFoundException;
import arollavengers.core.exceptions.user.UserNotFoundException;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.UnitOfWork;
import arollavengers.core.infrastructure.UnitOfWorkFactory;
import arollavengers.pattern.annotation.DependencyInjection;

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

    /**
     *
     * @param worldId
     * @param ownerId
     * @param difficulty
     */
    public void createWorld(Id worldId, Id ownerId, Difficulty difficulty) {
        UnitOfWork uow = unitOfWorkFactory.create();

        User user = userRepository.getUser(uow, ownerId);
        if (user == null) {
            throw new UserNotFoundException(ownerId);
        }

        World world = new World(worldId, uow);
        world.createWorld(user, difficulty);
        worldRepository.addWorld(uow, world);
        uow.commit();
    }

    public void joinGame(Id worldId, Id userId, MemberRole role) {
        UnitOfWork uow = unitOfWorkFactory.create();

        User user = userRepository.getUser(uow, userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }

        World world = worldRepository.getWorld(uow, worldId);
        if (world == null) {
            throw new WorldNotFoundException(worldId);
        }

        world.registerMember(user, role);
        user.joinGame(worldId, GameType.Pandemic);

        userRepository.addUser(uow, user);
        worldRepository.addWorld(uow, world);
        uow.commit();
    }

    /**
     * Define the {@link UnitOfWorkFactory} used by the service.
     *
     * @param unitOfWorkFactory
     */
    @DependencyInjection
    public void setUnitOfWorkFactory(UnitOfWorkFactory unitOfWorkFactory) {
        this.unitOfWorkFactory = unitOfWorkFactory;
    }

    /**
     * Define the {@link UserRepository} used by the service.
     *
     * @param userRepository
     */
    @DependencyInjection
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Define the {@link WorldRepository} used by the service.
     *
     * @param worldRepository
     */
    @DependencyInjection
    public void setWorldRepository(WorldRepository worldRepository) {
        this.worldRepository = worldRepository;
    }
}
