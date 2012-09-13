package arollavengers.core.service.user;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

import arollavengers.core.domain.user.User;
import arollavengers.core.domain.user.UserLoginIndex;
import arollavengers.core.domain.user.UserRepository;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.UnitOfWork;
import arollavengers.core.infrastructure.UnitOfWorkFactory;
import arollavengers.core.pattern.annotation.DependencyInjection;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import javax.inject.Inject;

@Service
public class UserService {

  @Inject
  private UnitOfWorkFactory unitOfWorkFactory;

  @Inject
  private UserRepository userRepository;

  @Inject
  private UserLoginIndex userLoginIndex;

  public User createUser(@NotNull UnitOfWork uow, @NotNull Id userId, @NotNull String login, char[] passwordDigest) {
    userLoginIndex.useLogin(uow, login, userId);

    User user = new User(uow);
    user.createUser(userId, login, passwordDigest, generateSalt());
    userRepository.addUser(uow, user);
    return user;
  }

  private char[] generateSalt() {
    String data = String.valueOf(System.currentTimeMillis());
    return md5Hex(data).toCharArray();
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
   * Define the {@link arollavengers.core.domain.user.UserRepositorySupport} used by the service.
   *
   * @param userRepository
   */
  @DependencyInjection
  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Define the {@link arollavengers.core.domain.user.UserLoginIndex} used by the service.
   *
   * @param userLoginIndex
   */
  @DependencyInjection
  public void setUserLoginIndex(UserLoginIndex userLoginIndex) {
    this.userLoginIndex = userLoginIndex;
  }
}

