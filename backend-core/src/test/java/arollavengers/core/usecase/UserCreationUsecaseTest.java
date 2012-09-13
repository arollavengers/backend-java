package arollavengers.core.usecase;

import static org.fest.assertions.api.Assertions.assertThat;

import arollavengers.core.domain.user.User;
import arollavengers.core.domain.user.UserLoginIndexSupport;
import arollavengers.core.domain.user.UserRepositorySupport;
import arollavengers.core.exceptions.user.LoginAlreadyInUseException;
import arollavengers.core.infrastructure.DummyUnitOfWork;
import arollavengers.core.infrastructure.EventStoreInMemory;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.UnitOfWork;
import arollavengers.core.infrastructure.UnitOfWorkFactory;
import arollavengers.core.service.user.UserService;

import org.junit.Test;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class UserCreationUsecaseTest {

  private UserService userService;
  private UserRepositorySupport userRepository;
  private UnitOfWorkFactory unitOfWorkFactory;
  private EventStoreInMemory eventStore;

  @Test
  public void create_user_then_commit_and_getIt() {
    // Given
    prepareEnvironment();

    // When
    UnitOfWork uow = unitOfWorkFactory.create();
    Id newUserId = Id.next();
    userService.createUser(uow, newUserId, "Travis", "Pacman".toCharArray());
    uow.commit();

    // Then
    uow = unitOfWorkFactory.create();
    User user = userRepository.getUser(uow, newUserId);
    assertThat(user).isNotNull();
    assertThat(user.login()).isEqualTo("Travis");

    // ~~~
    eventStore.dump(System.out);
  }

  @Test
  public void createUser_and_getUser_within_the_same_unitOfWork() {
    // Given
    prepareEnvironment();

    // When
    UnitOfWork uow = unitOfWorkFactory.create();
    Id newUserId = Id.next();
    User userCreated = userService.createUser(uow, newUserId, "Travis", "Pacman".toCharArray());

    // Then
    User user = userRepository.getUser(uow, newUserId);
    assertThat(user).isNotNull();
    assertThat(user).isSameAs(userCreated);
  }

  @Test
  public void getUser_returns_always_the_same_instance_within_the_same_unitOfWork() {
    // Given
    prepareEnvironment();

    // When
    Id newUserId = Id.next();
    UnitOfWork uow = unitOfWorkFactory.create();
    userService.createUser(uow, newUserId, "Travis", "Pacman".toCharArray());
    uow.commit();

    // Then
    uow = unitOfWorkFactory.create();
    User user1 = userRepository.getUser(uow, newUserId);
    assertThat(user1).isNotNull();
    assertThat(user1.login()).isEqualTo("Travis");

    User user2 = userRepository.getUser(uow, newUserId);
    assertThat(user2).isSameAs(user1);
  }

  @Test(expected = LoginAlreadyInUseException.class)
  public void create_user_with_login_already_used() {
    // Given
    prepareEnvironment();

    // When
    Id newUserId = Id.next();
    UnitOfWork uow = unitOfWorkFactory.create();
    userService.createUser(uow, newUserId, "Travis", "Pacman".toCharArray());
    uow.commit();

    Id newUserId2 = Id.next();
    uow = unitOfWorkFactory.create();
    userService.createUser(uow, newUserId2, "Travis", "Wahouu".toCharArray());
  }

  private void prepareEnvironment() {
    eventStore = new EventStoreInMemory();
    unitOfWorkFactory = new UnitOfWorkFactory() {
      @Override
      public UnitOfWork create() {
        return new DummyUnitOfWork();
      }
    };

    UserLoginIndexSupport userLoginIndex = new UserLoginIndexSupport();
    userLoginIndex.setEventStore(eventStore);

    userRepository = new UserRepositorySupport();
    userRepository.setEventStore(eventStore);

    userService = new UserService();
    userService.setUnitOfWorkFactory(unitOfWorkFactory);
    userService.setUserRepository(userRepository);
    userService.setUserLoginIndex(userLoginIndex);
  }

}
