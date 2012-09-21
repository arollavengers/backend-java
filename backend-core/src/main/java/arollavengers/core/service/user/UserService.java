package arollavengers.core.service.user;

import arollavengers.core.domain.user.User;
import arollavengers.core.domain.user.UserLoginIndex;
import arollavengers.core.domain.user.UserRepository;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.UnitOfWork;
import arollavengers.core.infrastructure.UnitOfWorkFactory;
import arollavengers.pattern.annotation.DependencyInjection;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import javax.inject.Inject;
import java.security.SecureRandom;

@Service
public class UserService {

    @Inject
    private UnitOfWorkFactory unitOfWorkFactory;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserLoginIndex userLoginIndex;

    /**
     *
     */
    public User createUser(@NotNull UnitOfWork uow, @NotNull Id userId, @NotNull String login, char[] passwordDigest) {
        userLoginIndex.useLogin(uow, login, userId);

        User user = new User(uow);
        user.createUser(userId, login, passwordDigest, generateSalt());
        userRepository.addUser(uow, user);
        return user;
    }

    /**
     *
     */
    public User login(@NotNull String login, char[] passwordDigest) {
        UnitOfWork uow = unitOfWorkFactory.create();
        Id userId = userLoginIndex.getByLogin(uow, login);
        if (userId == null) {
            return null;
        }

        User user = userRepository.getUser(uow, userId);
        if (user == null) {
            return null;
        }

        if (user.checkPassword(passwordDigest)) {
            return user;
        }
        else {
            return null;
        }
    }

    private byte[] generateSalt() {
        byte[] salt = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Define the {@link UnitOfWorkFactory} used by the service.
     */
    @DependencyInjection
    public void setUnitOfWorkFactory(UnitOfWorkFactory unitOfWorkFactory) {
        this.unitOfWorkFactory = unitOfWorkFactory;
    }

    /**
     * Define the {@link arollavengers.core.domain.user.UserRepositorySupport} used by the service.
     */
    @DependencyInjection
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Define the {@link arollavengers.core.domain.user.UserLoginIndex} used by the service.
     */
    @DependencyInjection
    public void setUserLoginIndex(UserLoginIndex userLoginIndex) {
        this.userLoginIndex = userLoginIndex;
    }
}

