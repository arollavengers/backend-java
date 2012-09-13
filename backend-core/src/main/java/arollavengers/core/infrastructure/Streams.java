package arollavengers.core.infrastructure;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Streams {

  public static <E> Stream<E> from(List<E> values) {
    return new StreamInMemory<E>(values);
  }

  public static <E> Stream<E> from(E... values) {
    return new StreamInMemory<E>(values);
  }

  public static <R, E> Stream<R> wrapAndCast(final Stream<E> source) {
    return new Stream<R>() {
      @Override
      public void consume(Function<R> function) {
        source.consume(Functions.<R,E>castAndForward(function));
      }
    };
  }

}
