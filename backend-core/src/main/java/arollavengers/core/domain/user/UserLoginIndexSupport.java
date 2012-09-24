package arollavengers.core.domain.user;

import arollavengers.core.exceptions.user.LoginAlreadyInUseException;
import arollavengers.core.infrastructure.AggregateRoot;
import arollavengers.core.infrastructure.AnnotationBasedEventHandler;
import arollavengers.core.infrastructure.DomainEvent;
import arollavengers.core.infrastructure.EventHandler;
import arollavengers.core.infrastructure.EventStore;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.Stream;
import arollavengers.core.infrastructure.UnitOfWork;
import arollavengers.core.infrastructure.annotation.OnEvent;
import arollavengers.pattern.annotation.DependencyInjection;
import com.google.common.collect.Maps;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import javax.inject.Inject;
import java.util.SortedMap;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class UserLoginIndexSupport implements UserLoginIndex {

    @Inject
    private EventStore eventStore;

    private Id loginIndexAggregateId = Id.create("user_login_index");

    @Override
    public void useLogin(UnitOfWork uow, String login, Id userId) {
        Index index = getIndex(uow);
        index.registerLogin(login, userId);
        uow.registerEventStoreFor(loginIndexAggregateId, eventStore);
    }

    @Override
    public void unuseLogin(UnitOfWork uow, String login, Id userId) {
        Index index = getIndex(uow);
        index.unregisterLogin(login);
        uow.registerEventStoreFor(loginIndexAggregateId, eventStore);
    }

    @Override
    public Id getByLogin(UnitOfWork uow, String login) {
        return getIndex(uow).getByLogin(login);
    }

    private Index getIndex(UnitOfWork uow) {
        Index index = new Index(loginIndexAggregateId, uow);
        Stream<LoginEvent> stream = eventStore.openStream(loginIndexAggregateId, LoginEvent.class);
        if (stream != null && stream.hasRemaining()) // first time the stream is null or empty
        {
            index.loadFromHistory(stream);
        }
        else {
            index.create(loginIndexAggregateId);
        }
        return index;
    }

    @DependencyInjection
    public void setEventStore(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    /**
     * Aggregate root used to manage index.
     *
     * @see LoginAddedEvent
     * @see LoginRemovedEvent
     */
    private static class Index extends AggregateRoot<LoginEvent> {

        private final EventHandler<LoginEvent> eventHandler;
        private final UnitOfWork uow;

        private SortedMap<String, Id> logins = Maps.newTreeMap();

        private Index(Id indexId, UnitOfWork uow) {
            super(indexId);
            this.uow = uow;
            this.eventHandler = new AnnotationBasedEventHandler<LoginEvent>(this);
        }

        public void create(Id indexId) {
            applyNewEvent(new LoginIndexCreatedEvent(indexId));
        }

        @OnEvent
        private void onCreate(LoginIndexCreatedEvent event) {
        }

        public void registerLogin(String login, Id userId) {
            if (logins.containsKey(login)) {
                throw new LoginAlreadyInUseException("Login: " + login);
            }
            applyNewEvent(new LoginAddedEvent(entityId(), login, userId));
        }

        @OnEvent
        private void doAddLogin(LoginAddedEvent event) {
            logins.put(event.login(), event.userId());
        }

        public void unregisterLogin(String login) {
            applyNewEvent(new LoginRemovedEvent(entityId(), login));
        }

        @OnEvent
        private void doRemoveLogin(LoginRemovedEvent event) {
            logins.remove(event.login());
        }

        public Id getByLogin(String login) {
            return logins.get(login);
        }

        @Override
        protected EventHandler<LoginEvent> internalEventHandler() {
            return eventHandler;
        }

        @Override
        protected UnitOfWork unitOfWork() {
            return uow;
        }

    }

    public static abstract class LoginEvent implements DomainEvent {

        @JsonProperty
        private long version;

        @JsonProperty
        private final Id indexId;

        protected LoginEvent(Id indexId) {
            if (indexId.isUndefined()) {
                throw new IllegalArgumentException("Id is undefied!");
            }
            this.indexId = indexId;
        }

        public long version() {
            return version;
        }

        public void assignVersion(final long version) {
            this.version = version;
        }

        @Override
        public Id entityId() {
            return indexId;
        }
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.CLASS,
            include = JsonTypeInfo.As.PROPERTY,
            property = "@class")
    public static class LoginAddedEvent extends LoginEvent {

        @JsonProperty
        private final String login;

        @JsonProperty
        private final Id userId;

        @JsonCreator
        public LoginAddedEvent(@JsonProperty("indexId") Id indexId,
                               @JsonProperty("login") String login,
                               @JsonProperty("userId") Id userId)
        {
            super(indexId);
            this.login = login;
            this.userId = userId;
        }

        public String login() {
            return login;
        }

        @Override
        public String toString() {
            return "LoginAddedEvent[" + entityId() + ", v" + version() + ", " + login() + "]";
        }

        public Id userId() {
            return userId;
        }
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.CLASS,
            include = JsonTypeInfo.As.PROPERTY,
            property = "@class")
    public static class LoginRemovedEvent extends LoginEvent {

        @JsonProperty
        private final String login;

        @JsonCreator
        public LoginRemovedEvent(@JsonProperty("indexId") Id indexId, @JsonProperty("login") String login) {
            super(indexId);
            this.login = login;
        }

        public String login() {
            return login;
        }

        @Override
        public String toString() {
            return "LoginRemovedEvent[" + entityId() + ", v" + version() + ", " + login() + "]";
        }
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.CLASS,
            include = JsonTypeInfo.As.PROPERTY,
            property = "@class")
    public static class LoginIndexCreatedEvent extends LoginEvent {

        @JsonCreator
        public LoginIndexCreatedEvent(@JsonProperty("indexId") Id indexId) {
            super(indexId);
        }

        @Override
        public String toString() {
            return "LoginIndexCreatedEvent[" + entityId() + ", v" + version() + "]";
        }
    }
}
