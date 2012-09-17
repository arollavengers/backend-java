package arollavengers.core.infrastructure;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    void store(@NotNull Id streamId, @NotNull Stream<DomainEvent> stream);

    /**
     *
     * @param streamId the id of the stream the events will be appended to
     * @param eventType type of the events (for generics...)
     * @param <E>
     * @return
     */
    @Nullable
    <E extends DomainEvent> Stream<E> openStream(@NotNull Id streamId, Class<E> eventType);

    /**
     * Debug method that dump the content of the event store.
     *
     * @param out
     */
    void dump(PrintStream out);
}
