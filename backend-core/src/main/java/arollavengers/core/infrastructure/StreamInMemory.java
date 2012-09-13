package arollavengers.core.infrastructure;

import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class StreamInMemory<E> implements Stream<E> {
  private static final Object[] EMPTY = new Object[0];

  private Object[] elements;

  public StreamInMemory(@NotNull List<E> values) {
    this.elements = values.toArray();
  }

  public StreamInMemory(E...values) {
    this.elements = values;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void consume(@NotNull Function<E> function) {
    int position = 0;
    try {
      for(;position<elements.length;position++) {
        function.apply((E)elements[position]);
      }
    }
    finally{
      // shrink consumed elements
      int newLength = elements.length-position;
      if(newLength==0) {
        elements = EMPTY;
      }
      else {
        Object[] newElements = new Object[newLength];
        System.arraycopy(elements, position, newElements, 0, newLength);
        elements = newElements;
      }
    }
  }
}
