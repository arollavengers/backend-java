package arollavengers.core.repository;

import arollavengers.core.entity.User;
import org.hibernate.dialect.Oracle10gDialect;
import org.junit.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManager;

import java.util.UUID;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

public class UserRepositoryTest {
  private static UserRepository userRepository;

  @BeforeClass
  public static void springStuff() {

    final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring/appContext-persistence.xml");
    userRepository = context.getBean("userRepository", UserRepository.class);
  }

  @Test
  public void find(){
    final String id = UUID.randomUUID().toString();
    final User user = new User(id,"jprudent@gmail.com");

    userRepository.save(user);

    final User foo = userRepository.find(id);
    assertThat(foo.getId()).isEqualTo(id);
  }

  @Test(expected = org.springframework.dao.DataAccessException.class)
  public void dbException(){
    final String id = UUID.randomUUID().toString();
    final User user = new User(id,"jprudent@gmail.com");

    userRepository.save(user);
    userRepository.save(user);

    fail("Any sql/jdbc related exception should be converted to an exception of DataAccessException hierarchy");
  }
}
