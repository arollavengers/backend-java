package arollavengers.core.infrastructure;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.PrintStream;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface EventStore {

  void store(Id streamId, Stream<DomainEvent> stream);

  @Nullable
  <E extends DomainEvent> Stream<E> openStream(@NotNull Id streamId, Class<E> eventType);

  void dump(PrintStream out);
}
