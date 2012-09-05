package arollavengers.core.infrastructure;

public interface EventHandler<E> {
  public void handle(final E event);
}
