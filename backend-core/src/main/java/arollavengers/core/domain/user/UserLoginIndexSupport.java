package arollavengers.core.domain.user;

import arollavengers.core.events.user.LoginAddedEvent;
import arollavengers.core.events.user.LoginEvent;
import arollavengers.core.events.user.LoginIndexCreatedEvent;
import arollavengers.core.events.user.LoginRemovedEvent;
import arollavengers.core.exceptions.user.LoginAlreadyInUseException;
import arollavengers.core.infrastructure.AggregateRoot;
import arollavengers.core.infrastructure.AnnotationBasedEventHandler;
import arollavengers.core.infrastructure.EventHandler;
import arollavengers.core.infrastructure.EventStore;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.Stream;
import arollavengers.core.infrastructure.UnitOfWork;
import arollavengers.core.infrastructure.annotation.OnEvent;
import arollavengers.pattern.annotation.DependencyInjection;
import com.google.common.collect.Maps;

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
     * @see arollavengers.core.events.user.LoginAddedEvent
     * @see arollavengers.core.events.user.LoginRemovedEvent
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

}
