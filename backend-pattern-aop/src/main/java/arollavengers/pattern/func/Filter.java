package arollavengers.pattern.func;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface Filter<T> {
    boolean isAccepted(T value);
}
