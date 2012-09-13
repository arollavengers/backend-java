package arollavengers.core.infrastructure;

import org.jetbrains.annotations.NotNull;
import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import java.io.Serializable;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class EventStorePrevayler implements EventStore {

  @Override
  public void store(Id streamId, Stream<DomainEvent> stream) {
  }

  @Override
  public <E extends DomainEvent> Stream<E> openStream(@NotNull Id streamId, Class<E> eventType) {
    throw new UnsupportedOperationException("not implemented yet");
  }

  private String dataFolder = "/Users/arnauld/Projects/arollavengers/backend-java/tmp/event_store";

  public void postConstruct() throws Exception {
    Prevayler prevayler =
            PrevaylerFactory.createPrevayler(
                    new PrevalentEventStream(), dataFolder);
    prevayler.
  }

  public static final class PrevalentEventStream implements Serializable {

  }
}
