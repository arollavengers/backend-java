package arollavengers.core.infrastructure;

public interface Stream<E> {
    /**
     * @return <code>true</code> if, and only if, there is at least one element remaining in this stream
     */
    boolean hasRemaining();

    void consume(Function<E> function);
}
