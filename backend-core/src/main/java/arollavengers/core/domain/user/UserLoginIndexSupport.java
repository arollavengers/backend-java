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
import arollavengers.core.pattern.annotation.DependencyInjection;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import javax.inject.Inject;
import java.util.TreeSet;

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
    index.addLogin(login);
    uow.registerEventStoreFor(loginIndexAggregateId, eventStore);
  }

  @Override
  public void unuseLogin(UnitOfWork uow, String login, Id userId) {
    Index index = getIndex(uow);
    index.removeLogin(login);
    uow.registerEventStoreFor(loginIndexAggregateId, eventStore);
  }

  private Index getIndex(UnitOfWork uow) {
    Index index = new Index(uow);
    Stream<LoginEvent> stream = eventStore.openStream(loginIndexAggregateId, LoginEvent.class);
    if (stream != null) // first time the stream is null
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

    private TreeSet<String> logins = new TreeSet<String>();

    private Index(UnitOfWork uow) {
      this.uow = uow;
      this.eventHandler = new AnnotationBasedEventHandler<LoginEvent>(this);
    }

    public void create(Id indexId) {
      applyNewEvent(new LoginIndexCreatedEvent(indexId));
    }

    @OnEvent
    private void onCreate(LoginIndexCreatedEvent event) {
      assignId(event.aggregateId());
    }

    public void addLogin(String login) {
      if (logins.contains(login)) {
        throw new LoginAlreadyInUseException("Login: " + login);
      }
      applyNewEvent(new LoginAddedEvent(aggregateId(), login));
    }

    @OnEvent
    private void doAddLogin(LoginAddedEvent event) {
      logins.add(event.login());
    }

    public void removeLogin(String login) {
      applyNewEvent(new LoginRemovedEvent(aggregateId(), login));
    }

    @OnEvent
    private void doRemoveLogin(LoginRemovedEvent event) {
      logins.remove(event.login());
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
      this.indexId = indexId;
    }

    public long version() {
      return version;
    }

    public void assignVersion(final long version) {
      this.version = version;
    }

    @Override
    public Id aggregateId() {
      return indexId;
    }
  }

  public static class LoginAddedEvent extends LoginEvent {

    @JsonProperty
    private final String login;

    @JsonCreator
    public LoginAddedEvent(@JsonProperty("indexId") Id indexId, @JsonProperty("login") String login) {
      super(indexId);
      this.login = login;
    }

    public String login() {
      return login;
    }

    @Override
    public String toString() {
      return "LoginAddedEvent[" + aggregateId() + ", v" + version() + ", " + login() + "]";
    }
  }

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
      return "LoginRemovedEvent[" + aggregateId() + ", v" + version() + ", " + login() + "]";
    }
  }

  public static class LoginIndexCreatedEvent extends LoginEvent {

    @JsonCreator
    public LoginIndexCreatedEvent(@JsonProperty("indexId") Id indexId) {
      super(indexId);
    }

    @Override
    public String toString() {
      return "LoginIndexCreatedEvent[" + aggregateId() + ", v" + version() + "]";
    }
  }
}
