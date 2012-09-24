package arollavengers.core.infrastructure;

import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SimpleBus implements Bus {
    private CopyOnWriteArrayList<Listener> listeners = Lists.newCopyOnWriteArrayList();

    @Override
    public void publish(@Nonnull Message message) {
        for(Listener listener : listeners)
            listener.onMessage(message);
    }

    @Override
    public void subscribe(@Nonnull Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void unsubscribe(@Nonnull Listener listener) {
        listeners.remove(listener);
    }
}
