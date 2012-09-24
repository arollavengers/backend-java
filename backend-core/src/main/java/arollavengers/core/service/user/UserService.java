package arollavengers.core.service.user;

import arollavengers.core.domain.user.User;
import arollavengers.core.domain.user.UserLoginIndex;
import arollavengers.core.domain.user.UserRepository;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.UnitOfWork;
import arollavengers.core.infrastructure.UnitOfWorkFactory;
import arollavengers.pattern.annotation.DependencyInjection;

import javax.annotation.Nonnull;
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
    public User createUser(@Nonnull UnitOfWork uow, @Nonnull Id userId, @Nonnull String login, char[] passwordDigest) {
        userLoginIndex.useLogin(uow, login, userId);

        User user = new User(userId, uow);
        user.createUser(login, passwordDigest, generateSalt());
        userRepository.addUser(uow, user);
        return user;
    }

    /**
     * Check the provided credentials and returns the corresponding user's {@link Id} if
     * they are valid.
     *
     * @param login login of the user that attempts to get logged
     * @param password plain password for the corresponding login
     * @return the corresponding user's {@link Id} is the credentials are fulfilled otherwise
     *    returns <code>null</code>
     */
    public Id getUserWithCredentials(@Nonnull String login, char[] password) {
        UnitOfWork uow = unitOfWorkFactory.create();
        Id userId = userLoginIndex.getByLogin(uow, login);
        if (userId == null) {
            return null;
        }

        User user = userRepository.getUser(uow, userId);
        if (user == null) {
            return null;
        }

        if (user.checkPassword(password)) {
            return userId;
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

