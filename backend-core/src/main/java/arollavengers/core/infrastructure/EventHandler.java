package arollavengers.core.infrastructure;

/**
 *
 * @param <E>
 */
public interface EventHandler<E> {
    /**
     * @param event the event that must be handled
     */
    void handle(final E event, Object...args);
}
