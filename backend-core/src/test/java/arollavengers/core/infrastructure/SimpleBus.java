package arollavengers.core.infrastructure;

import com.google.common.collect.Lists;

import org.jetbrains.annotations.NotNull;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SimpleBus implements Bus {
    private CopyOnWriteArrayList<Listener> listeners = Lists.newCopyOnWriteArrayList();

    @Override
    public void publish(@NotNull Message message) {
        for(Listener listener : listeners)
            listener.onMessage(message);
    }

    @Override
    public void subscribe(@NotNull Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void unsubscribe(@NotNull Listener listener) {
        listeners.remove(listener);
    }
}
