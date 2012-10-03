package arollavengers.core.infrastructure;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintStream;

/**
 * Event store.
 *
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface EventStore {

    /**
     * Store the stream of events. The stream's id is usually the aggregate's id the events belong to.
     * @param streamId the id of the stream the events will be appended to
     * @param stream the stream of event to store
     */
    void store(@Nonnull Id streamId, @Nonnull Stream<VersionedDomainEvent<?>> stream);

    /**
     *
     * @param streamId the id of the stream the events will be appended to
     * @param eventType type of the events (for generics...)
     */
    @Nullable
    <E extends DomainEvent> Stream<VersionedDomainEvent<E>> openStream(@Nonnull Id streamId, Class<E> eventType);

    /**
     * Debug method that dump the content of the event store.
     *
     * @param out output
     */
    // TODO move me in a more generic interface e.g. Debugable
    void dump(PrintStream out);
}
