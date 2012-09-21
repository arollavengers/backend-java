package arollavengers.core.util;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Functions {
  public static <R,E> Function<E> castAndForward(final Function<R> function) {
    return new Function<E>() {
      @SuppressWarnings("unchecked")
      @Override
      public void apply(E e) {
        function.apply((R)e);
      }
    };
  }
}
