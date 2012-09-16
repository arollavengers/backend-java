package arollavengers.core.infrastructure;

import arollavengers.core.pattern.annotation.DependencyInjection;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.jetbrains.annotations.NotNull;
import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.Query;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.PostConstruct;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class EventStorePrevayler implements EventStore {

  private Prevayler prevayler;

  @Value("${event-store.prevayler.data-folder}")
  private String dataFolder;

  @Override
  public void store(@NotNull Id streamId, @NotNull Stream<DomainEvent> stream) {
    List<DomainEvent> events = Streams.toList(stream);
    prevayler.execute(new AppendStream(streamId, events.toArray(new DomainEvent[events.size()])));
  }

  @SuppressWarnings("unchecked")
  @Override
  public <E extends DomainEvent> Stream<E> openStream(@NotNull Id streamId, @NotNull Class<E> eventType) {
    try {
      return (Stream<E>) prevayler.execute(new QueryStream<E>(streamId, eventType));
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void dump(final PrintStream out) {
    try {
      prevayler.execute(new Query() {

        @Override
        public Object query(Object prevalentSystem, Date executionTime) {
          PrevalentEventStore eventStore = (PrevalentEventStore) prevalentSystem;
          for(Map.Entry<Id,List<DomainEvent>> entry : eventStore.eventsPerStream.entrySet()) {
            out.println(entry.getKey() + ": ");
            for (DomainEvent event : entry.getValue()) {
              out.println("  . " + event);
            }
          }
          return null;
        }
      });
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @PostConstruct
  public void postConstruct() throws Exception {
    if(dataFolder==null)
      throw new BeanInitializationException("No data folder defined");
    System.out.println("EventStorePrevayler.postConstruct(" + dataFolder + ")");

    JacksonSerializer jacksonSerializer = new JacksonSerializer();
    jacksonSerializer.postConstruct();

    PrevaylerFactory factory = new PrevaylerFactory();
    factory.configurePrevalentSystem(new PrevalentEventStore());
    factory.configurePrevalenceDirectory(dataFolder);
    factory.configureJournalSerializer("journal", jacksonSerializer);
    prevayler = factory.create();
  }

  @DependencyInjection
  public void setDataFolder(String dataFolder) {
    this.dataFolder = dataFolder;
  }

  public static final class PrevalentEventStore implements Serializable {
    private Map<Id, List<DomainEvent>> eventsPerStream = Maps.newHashMap();

    public void appendOrCreate(Id streamId, DomainEvent... stream) {
      appendOrCreate(streamId, Arrays.asList(stream));
    }
    public void appendOrCreate(Id streamId, List<DomainEvent> stream) {
      List<DomainEvent> events = eventsPerStream.get(streamId);
      if (events == null) {
        events = Lists.newArrayList();
        eventsPerStream.put(streamId, events);
      }
      events.addAll(stream);
    }

    private Function<DomainEvent> appendTo(final List<DomainEvent> events) {
      return new Function<DomainEvent>() {
        @Override
        public void apply(DomainEvent event) {
          events.add(event);
        }
      };
    }

    public <E extends DomainEvent> Stream<E> openStream(Id streamId, Class<E> eventType) {
      List<DomainEvent> events = eventsPerStream.get(streamId);
      if (events == null) {
        //throw new StreamNotFoundException("Stream Id: " + streamId);
        return null;
      }
      return Streams.wrapAndCast(Streams.from(events));
    }
  }

  @JsonTypeInfo(
          use = JsonTypeInfo.Id.CLASS,
          include = JsonTypeInfo.As.PROPERTY,
          property = "@class")
  public static class QueryStream<E extends DomainEvent> implements Query {
    private final Id streamId;
    private final Class<E> eventType;

    public QueryStream(Id streamId, Class<E> eventType) {
      this.streamId = streamId;
      this.eventType = eventType;
    }

    @Override
    public Object query(Object prevalentSystem, Date executionTime) {
      PrevalentEventStore eventStore = (PrevalentEventStore) prevalentSystem;
      return eventStore.openStream(streamId, eventType);
    }
  }

  @JsonTypeInfo(
          use = JsonTypeInfo.Id.CLASS,
          include = JsonTypeInfo.As.PROPERTY,
          property = "@class")
  public static class AppendStream implements org.prevayler.Transaction {

    @JsonProperty
    private final Id streamId;

    @JsonProperty
    private final DomainEvent[] events;

    @JsonCreator
    public AppendStream(
            @JsonProperty("streamId") Id streamId,
            @JsonProperty("events") DomainEvent[] events)
    {
      this.streamId = streamId;
      this.events = events;
    }

    @Override
    public void executeOn(Object prevalentSystem, Date executionTime) {
      PrevalentEventStore eventStore = (PrevalentEventStore) prevalentSystem;
      eventStore.appendOrCreate(streamId, events);
    }
  }
}
