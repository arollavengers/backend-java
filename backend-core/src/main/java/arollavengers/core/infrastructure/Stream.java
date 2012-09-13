package arollavengers.core.infrastructure;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public interface Stream<E> {
  void consume(Function<E> function);
}
