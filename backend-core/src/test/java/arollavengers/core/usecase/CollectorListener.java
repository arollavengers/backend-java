package arollavengers.core.usecase;

import arollavengers.core.infrastructure.Bus;
import arollavengers.core.infrastructure.Message;
import com.google.common.collect.Lists;

import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CollectorListener implements Bus.Listener {

    private List<Message> messages = Lists.newArrayList();

    @Override
    public void onMessage(@NotNull Message message) {
        messages.add(message);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void clearMessages() {
        messages.clear();
    }
}
