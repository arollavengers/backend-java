package arollavengers.core.domain.user;

import arollavengers.core.events.user.UserCreatedEvent;
import arollavengers.core.events.user.UserEvent;
import arollavengers.core.exceptions.EntityIdAlreadyAssignedException;
import arollavengers.core.infrastructure.AggregateRoot;
import arollavengers.core.infrastructure.AnnotationBasedEventHandler;
import arollavengers.core.infrastructure.EventHandler;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.UnitOfWork;
import arollavengers.core.infrastructure.annotation.OnEvent;
import arollavengers.core.util.Bytes;

import org.jetbrains.annotations.NotNull;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class User extends AggregateRoot<UserEvent> {

    private final UnitOfWork uow;
    private final EventHandler<UserEvent> eventHandler;

    // ~~~ User state
    private String login;
    private byte[] passwordDigest;
    private byte[] salt;

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
     * Returns the login of the user.
     *
     * @return the login of the user.
     */
    public String login() {
        return login;
    }

    /**
     * Create a new user.
     *
     * @param newId          Id of the user
     * @param login          login of the user
     * @param password       password
     * @param salt           salt to use to digest the password
     */
    public void createUser(@NotNull Id newId, @NotNull String login, char[] password, byte[] salt) {
        if (!aggregateId().isUndefined()) {
            throw new EntityIdAlreadyAssignedException(aggregateId(), newId);
        }
        passwordDigest = digest(password, salt);
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

    private static byte[] digest(char[] password, byte[] salt) {
        MessageDigest digest = getDigest("SHA-512");
        digest.update(Bytes.toBytes(password));
        digest.update(salt);
        return digest.digest();
    }

    private static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean checkPassword(char[] password) {
        byte[] submittedDigest = digest(password, salt);
        return Arrays.equals(submittedDigest, passwordDigest);
    }
}
