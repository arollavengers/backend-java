package arollavengers.core.domain.user;

import arollavengers.core.events.user.UserEvent;
import arollavengers.core.infrastructure.EventStore;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.Stream;
import arollavengers.core.infrastructure.UnitOfWork;
import arollavengers.pattern.annotation.DependencyInjection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class UserRepositorySupport implements UserRepository {

    @Inject
    private EventStore eventStore;

    /**
     * Add the user to the repository.
     *
     * @param uow  unit of work in which the user will be attached
     * @param user the user
     */
    @Override
    public void addUser(@Nonnull UnitOfWork uow, @Nonnull User user) {
        uow.registerEventStoreFor(user.entityId(), eventStore);
        uow.attach(user);
    }

    /**
     * Return the user (if it exists) with the specified id.
     *
     * @param uow    unit of work in which the loaded user will be attached
     * @param userId the user's id
     */
    @Override
    @Nullable
    public User getUser(@Nonnull UnitOfWork uow, @Nonnull Id userId) {
        User user = uow.getAggregate(userId);
        if (user == null) {
            Stream<UserEvent> stream = eventStore.openStream(userId, UserEvent.class);
            if (stream == null) {
                return null;
            }
            user = new User(userId, uow);
            user.loadFromHistory(stream);
            uow.attach(user);
        }
        return user;
    }

    @DependencyInjection
    public void setEventStore(EventStore eventStore) {
        this.eventStore = eventStore;
    }


}
