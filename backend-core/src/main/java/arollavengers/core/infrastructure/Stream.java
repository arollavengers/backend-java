package arollavengers.core.infrastructure;

import java.util.Collections;
import java.util.List;

public class Stream<E extends DomainEvent> {

  List<E> elements = Collections.emptyList();

  public void foreach(final Function<E> function) {
    for (E e : elements) {
      function.apply(e);
    }
  }
}
