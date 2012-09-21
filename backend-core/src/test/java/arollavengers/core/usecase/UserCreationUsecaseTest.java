package arollavengers.core.usecase;

import static org.fest.assertions.api.Assertions.assertThat;

import arollavengers.core.TestSettings;
import arollavengers.core.domain.user.User;
import arollavengers.core.domain.user.UserLoginIndexSupport;
import arollavengers.core.domain.user.UserRepositorySupport;
import arollavengers.core.exceptions.user.LoginAlreadyInUseException;
import arollavengers.core.infrastructure.DummyUnitOfWork;
import arollavengers.core.infrastructure.EventStore;
import arollavengers.core.infrastructure.eventstore.EventStoreInMemory;
import arollavengers.core.infrastructure.eventstore.EventStoreJdbc;
import arollavengers.core.infrastructure.eventstore.EventStorePrevayler;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.Message;
import arollavengers.core.infrastructure.SimpleBus;
import arollavengers.core.infrastructure.UnitOfWork;
import arollavengers.core.infrastructure.UnitOfWorkFactory;
import arollavengers.core.service.user.UserService;
import arollavengers.core.util.Objects;
import arollavengers.junit.LabeledParameterized;
import com.google.common.collect.Lists;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
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
@SuppressWarnings("ConstantConditions")
@RunWith(LabeledParameterized.class)
public class UserCreationUsecaseTest {

    /**
     * Test will be executed on all different event store implementations.
     * @see TypeOfEventStore
     */
    @LabeledParameterized.Parameters
    public static List<Object[]> parameters() {
        List<Object[]> modes = Lists.newArrayList();
        for (TypeOfEventStore typeOfEventStore : TypeOfEventStore.values()) {
            modes.add(Objects.o(typeOfEventStore));
        }
        return modes;
    }

    private final TypeOfEventStore typeOfEventStore;
    //
    private TestSettings testSettings;
    //
    private UserService userService;
    private UserRepositorySupport userRepository;
    private UnitOfWorkFactory unitOfWorkFactory;
    private SimpleBus bus;
    private CollectorListener collectorListener;

    public UserCreationUsecaseTest(TypeOfEventStore typeOfEventStore) {
        this.typeOfEventStore = typeOfEventStore;
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
        // eventStore.dump(System.out);
        // dumpMessages();

        // Then
        uow = unitOfWorkFactory.create();
        User user = userRepository.getUser(uow, newUserId);
        assertThat(user).isNotNull();
        assertThat(user.login()).isEqualTo("Travis");

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

    @Test
    public void create_user_with_login_already_used() throws Exception {
        // Given
        prepareEnvironment();

        // When
        Id newUserId = Id.next();
        UnitOfWork uow = unitOfWorkFactory.create();
        userService.createUser(uow, newUserId, "Travis", "Pacman".toCharArray());
        uow.commit();

        collectorListener.clearMessages();

        // Then
        Id newUserId2 = Id.next();
        uow = unitOfWorkFactory.create();
        try {
            userService.createUser(uow, newUserId2, "Travis", "Wahouu".toCharArray());
            Assert.fail("An exception should have been raised");
        }
        catch (LoginAlreadyInUseException alreadyInUse) {
            // great!
        }
        assertThat(collectorListener.getMessages()).isEmpty();
    }

    @SuppressWarnings("UnusedDeclaration")
    private void dumpMessages () {
        System.out.println("UserCreationUsecaseTest.dumpMessages:");
        for (Message message : collectorListener.getMessages()) {
            System.out.println(">> " + message);
        }
    }

    @SuppressWarnings("unchecked")
    private void prepareEnvironment() throws Exception {
        EventStore eventStore = typeOfEventStore.eventStore(testSettings);
        collectorListener = new CollectorListener();
        bus = new SimpleBus();
        bus.subscribe(collectorListener);

        unitOfWorkFactory = new UnitOfWorkFactory() {
            @Override
            public UnitOfWork create() {
                return new DummyUnitOfWork(bus);
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
        for (String sql : script.split(";")) {
            jdbcTemplate.update(sql.trim());
        }
    }

    public enum TypeOfEventStore {
        InMemory
                {
                    @Override
                    EventStore eventStore(TestSettings testSettings) {
                        return new EventStoreInMemory();
                    }
                },
        Prevayler
                {
                    @Override
                    EventStore eventStore(TestSettings testSettings) throws Exception {
                        EventStorePrevayler eventStore = new EventStorePrevayler();
                        String dataFolder =
                                testSettings.getProperty("prevayler.event-store.basedir") + "/"
                                        + UUID.randomUUID().toString();
                        eventStore.setDataFolder(dataFolder);
                        eventStore.postConstruct();
                        return eventStore;
                    }
                },
        Jdbc
                {
                    @SuppressWarnings("unchecked")
                    @Override
                    EventStore eventStore(TestSettings testSettings) throws Exception {
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

                        EventStoreJdbc eventStore = new EventStoreJdbc();
                        eventStore.setDataSource(dataSource);
                        eventStore.postConstruct();
                        return eventStore;
                    }
                };
        //Hibernate

        abstract EventStore eventStore(TestSettings testSettings) throws Exception;

    }


}
