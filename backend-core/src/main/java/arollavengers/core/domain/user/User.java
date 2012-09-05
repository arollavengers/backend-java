package arollavengers.core.domain.user;

import arollavengers.core.events.pandemic.WorldCreatedEvent;
import arollavengers.core.events.user.UserCreatedEvent;
import arollavengers.core.events.user.UserEvent;
import arollavengers.core.exceptions.EntityIdAlreadyAssignedException;
import arollavengers.core.infrastructure.AggregateRoot;
import arollavengers.core.infrastructure.AnnotationBasedEventHandler;
import arollavengers.core.infrastructure.EventHandler;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.UnitOfWork;
import arollavengers.core.infrastructure.annotation.OnEvent;

public class User extends AggregateRoot<UserEvent> {

    private final UnitOfWork uow;
    private final EventHandler<UserEvent> eventHandler;

    // ~~~ User state
    private String login;
    private char[] passwordDigest;
    private char[] salt;

    public User(UnitOfWork uow) {
        this.uow = uow;
        this.eventHandler = new AnnotationBasedEventHandler<UserEvent>(this);
    }

    @Override
    protected EventHandler<UserEvent> internalEventHandler() {
        return eventHandler;
    }

    @Override
    protected UnitOfWork unitOfWork() {
        return uow;
    }

    /**
     * Create a new user.
     *
     * @param newId Id of the user
     * @param login login of the user
     * @param passwordDigest password digest
     * @param salt salt used to digest the password
     */
    public void createUser(Id newId, String login, char[] passwordDigest, char[] salt) {
        if (!aggregateId().isUndefined()) {
            throw new EntityIdAlreadyAssignedException(aggregateId(), newId);
        }
        UserCreatedEvent createdEvent = new UserCreatedEvent(newId, login, passwordDigest, salt);
        applyNewEvent(createdEvent);
    }

    @OnEvent
    private void doCreateUser(UserCreatedEvent event) {
        assignId(event.aggregateId());
        this.login = event.login();
        this.passwordDigest = event.passwordDigest();
        this.salt = event.salt();
    }
}
