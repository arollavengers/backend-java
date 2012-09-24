package arollavengers.core.domain.pandemic;

import arollavengers.core.events.pandemic.WorldEvent;
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
public class WorldRepositorySupport implements WorldRepository {

    @Inject
    private EventStore eventStore;

    /**
     * Add the world to the repository.
     *
     * @param uow   unit of work in which the world will be attached
     * @param world the world
     */
    @Override
    public void addWorld(@Nonnull UnitOfWork uow, @Nonnull World world) {
        uow.registerEventStoreFor(world.entityId(), eventStore);
        uow.attach(world);
    }

    /**
     * Return the world (if it exists) with the specified id.
     *
     * @param uow     unit of work in which the loaded world will be attached
     * @param worldId the world's id
     */
    @Override
    @Nullable
    public World getWorld(@Nonnull UnitOfWork uow, @Nonnull Id worldId) {
        World world = uow.getAggregate(worldId);
        if (world == null) {
            Stream<WorldEvent> stream = eventStore.openStream(worldId, WorldEvent.class);
            if (stream == null) {
                return null;
            }
            world = new World(worldId, uow);
            world.loadFromHistory(stream);

            uow.registerEventStoreFor(world.entityId(), eventStore);
            uow.attach(world);
        }
        return world;
    }

    @DependencyInjection
    public void setEventStore(EventStore eventStore) {
        this.eventStore = eventStore;
    }
}
