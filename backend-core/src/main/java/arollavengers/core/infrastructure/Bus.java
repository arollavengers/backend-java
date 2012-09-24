package arollavengers.core.infrastructure;

import javax.annotation.Nonnull;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface Bus {
    void publish(@Nonnull Message message);
    void subscribe(@Nonnull Listener listener);
    void unsubscribe(@Nonnull Listener listener);

    public interface Listener {
      void onMessage(@Nonnull Message message);
    }
}
