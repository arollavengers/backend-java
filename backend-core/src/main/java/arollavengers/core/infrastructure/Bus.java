package arollavengers.core.infrastructure;

import org.jetbrains.annotations.NotNull;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface Bus {
    void publish(@NotNull Message message);
    void subscribe(@NotNull Listener listener);
    void unsubscribe(@NotNull Listener listener);

    public interface Listener {
      void onMessage(@NotNull Message message);
    }
}
