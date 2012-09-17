package arollavengers.core.usecase;

import static org.fest.assertions.api.Assertions.assertThat;

import arollavengers.core.TestSettings;
import arollavengers.core.domain.user.User;
import arollavengers.core.domain.user.UserLoginIndexSupport;
import arollavengers.core.domain.user.UserRepositorySupport;
import arollavengers.core.exceptions.user.LoginAlreadyInUseException;
import arollavengers.core.infrastructure.DummyUnitOfWork;
import arollavengers.core.infrastructure.EventStore;
import arollavengers.core.infrastructure.EventStoreInMemory;
import arollavengers.core.infrastructure.EventStoreJdbc;
import arollavengers.core.infrastructure.EventStorePrevayler;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.UnitOfWork;
import arollavengers.core.infrastructure.UnitOfWorkFactory;
import arollavengers.core.service.user.UserService;
import arollavengers.junit.LabeledParameterized;
import com.google.common.collect.Lists;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Driver;
import java.util.List;
import java.util.UUID;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@RunWith(LabeledParameterized.class)
public class UserCreationUsecaseTest {

    public enum Mode {
        InMemory,
        Prevayler,
        Jdbc,
        Hibernate
    }

    @LabeledParameterized.Parameters
    public static List<Object[]> parameters() {
        List<Object[]> modes = Lists.newArrayList();
        for (Mode mode : Mode.values()) {
            modes.add(o(mode));
        }
        return modes;
    }

    private static Object[] o(Object... args) {
        return args;
    }

    private final Mode mode;
    //
    private UserService userService;
    private UserRepositorySupport userRepository;
    private UnitOfWorkFactory unitOfWorkFactory;
    private EventStore eventStore;
    private TestSettings testSettings;

    public UserCreationUsecaseTest(Mode mode) {
        this.mode = mode;
    }

    @Before
    public void setUp() throws IOException {
        testSettings = new TestSettings();
    }

    @Test
    public void create_user_then_commit_and_getIt() throws Exception {
        // Given
        prepareEnvironment();

        // When
        UnitOfWork uow = unitOfWorkFactory.create();
        Id newUserId = Id.next();
        userService.createUser(uow, newUserId, "Travis", "Pacman".toCharArray());
        uow.commit();

        // ~~~
        eventStore.dump(System.out);

        // Then
        uow = unitOfWorkFactory.create();
        User user = userRepository.getUser(uow, newUserId);
        assertThat(user).isNotNull();
        assertThat(user.login()).isEqualTo("Travis");

        // ~~~
        eventStore.dump(System.out);
    }

    @Test
    public void createUser_and_getUser_within_the_same_unitOfWork() throws Exception {
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
    public void getUser_returns_always_the_same_instance_within_the_same_unitOfWork() throws Exception {
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
    public void create_user_with_login_already_used() throws Exception {
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

    @SuppressWarnings("unchecked")
    private void prepareEnvironment() throws Exception {
        switch (mode) {
            case Prevayler:
                eventStore = new EventStorePrevayler();
                String dataFolder =
                        testSettings.getProperty("prevayler.event-store.basedir") + "/" + UUID.randomUUID().toString();
                ((EventStorePrevayler) eventStore).setDataFolder(dataFolder);
                ((EventStorePrevayler) eventStore).postConstruct();
                break;
            case Jdbc:
                SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
                dataSource.setDriverClass((Class<? extends Driver>) Class.forName(testSettings.getProperty("jdbc.driver")));
                dataSource.setUrl(testSettings.getProperty("jdbc.url"));
                for (String script : testSettings.getProperty("jdbc.scripts").split(",")) {
                    FileInputStream inputStream = new FileInputStream(script.trim());
                    try {
                        executeScriptOn(dataSource, IOUtils.toString(inputStream, "utf-8"));
                    }
                    finally {
                        IOUtils.closeQuietly(inputStream);
                    }
                }

                eventStore = new EventStoreJdbc();
                ((EventStoreJdbc) eventStore).setDataSource(dataSource);
                ((EventStoreJdbc) eventStore).postConstruct();
                break;
            case Hibernate:
            case InMemory:
                eventStore = new EventStoreInMemory();
                break;
        }

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

    private static void executeScriptOn(DataSource dataSource, String script) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        for(String sql : script.split(";")) {
            jdbcTemplate.update(sql.trim());
        }
    }

}
