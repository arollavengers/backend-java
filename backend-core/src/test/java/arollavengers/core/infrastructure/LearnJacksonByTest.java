package arollavengers.core.infrastructure;

import static org.fest.assertions.api.Assertions.assertThat;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.deser.std.StdDeserializer;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

/**
 *
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class LearnJacksonByTest {

  @Test
  public void serialize_event() throws IOException {
    MyEvent event = new MyEvent("Travis", "Pacman".toCharArray());

    StringWriter writer = new StringWriter();

    ObjectMapper mapper = new ObjectMapper().enableDefaultTyping();
    ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
    objectWriter.writeValue(writer, event);

    System.out.println(writer.toString());
  }

  @Test
  public void deserialize_event_annotationMixin() throws IOException {
    String event = "{\n" +
            "  \"@class\" : \"arollavengers.core.infrastructure.LearnJacksonByTest$MyEvent\",\n" +
            "  \"version\" : 0,\n" +
            "  \"id\" : \"4241febf-4901-43b6-b8ed-c9a75770fbb6\",\n" +
            "  \"login\" : \"Travis\",\n" +
            "  \"password\" : \"Pacman\"\n" +
            "}";

    ObjectMapper mapper = new ObjectMapper().enableDefaultTyping();

    DeserializationConfig deserializationConfig = mapper.getDeserializationConfig();
    deserializationConfig.addMixInAnnotations(DomainEvent.class, PolymorphicEventMixIn.class);

    Object o = mapper.readValue(event, DomainEvent.class);
    assertThat(o).isInstanceOf(MyEvent.class);
    MyEvent myEvent = (MyEvent) o;
    assertThat(myEvent.login()).isEqualTo("Travis");
    assertThat(myEvent.password()).isEqualTo("Pacman".toCharArray());
    assertThat(myEvent.entityId()).isEqualTo(Id.create("4241febf-4901-43b6-b8ed-c9a75770fbb6"));
  }

  @Test
  public void deserialize_event_customDeserializer() throws IOException {
    String event = "{\n" +
            "  \"@class\" : \"arollavengers.core.infrastructure.LearnJacksonByTest$MyEvent\",\n" +
            "  \"version\" : 0,\n" +
            "  \"id\" :  \"4241febf-4901-43b6-b8ed-c9a75770fbb6\",\n" +
            "  \"login\" : \"Travis\",\n" +
            "  \"password\" : \"Pacman\"\n" +
            "}";

    SimpleModule module = new SimpleModule("events", new Version(1, 0, 0, null));
    module.addDeserializer(DomainEvent.class, new EventDeserializer());

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(module);

    Object o = mapper.readValue(event, DomainEvent.class);
    assertThat(o).isInstanceOf(MyEvent.class);
    MyEvent myEvent = (MyEvent) o;
    assertThat(myEvent.login()).isEqualTo("Travis");
    assertThat(myEvent.password()).isEqualTo("Pacman".toCharArray());
    assertThat(myEvent.entityId()).isEqualTo(Id.create("4241febf-4901-43b6-b8ed-c9a75770fbb6"));
  }

  // --------------------------------------------------------------------------

  public static class EventDeserializer extends StdDeserializer<DomainEvent> {
    protected EventDeserializer() {
      super(DomainEvent.class);
    }

    @Override
    public DomainEvent deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
      ObjectMapper mapper = (ObjectMapper) jp.getCodec();
      ObjectNode root = (ObjectNode) mapper.readTree(jp);

      // remove type info otherwise it cannot be matched to concrete class field
      JsonNode removed = root.remove("@class");
      if (removed == null) {
        throw new RuntimeException("No type information");
      }
      String klazzName = removed.getTextValue();

      try {
        Class<?> klazz = Class.forName(klazzName);
        return (DomainEvent) mapper.readValue(root, klazz);
      }
      catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
  }

  // --------------------------------------------------------------------------

  @JsonTypeInfo(
          use = JsonTypeInfo.Id.CLASS,
          include = JsonTypeInfo.As.PROPERTY,
          property = "@class")
  @JsonSubTypes({
          @JsonSubTypes.Type(value = MyEvent.class)
  })
  abstract class PolymorphicEventMixIn {
  }

  // --------------------------------------------------------------------------

  public static class MyEvent implements DomainEvent {

    @JsonProperty
    private long version;

    @JsonProperty
    private Id id = Id.next(MyEvent.class);

    @JsonProperty
    private final String login;

    @JsonProperty
    private final char[] password;

    @JsonCreator
    public MyEvent(@JsonProperty("login") String login, @JsonProperty("password") char[] password) {
      this.login = login;
      this.password = password;
    }

    @Override
    public long version() {
      return version;
    }

    @Override
    public Id entityId() {
      return id;
    }

    @Override
    public void assignVersion(long newVersion) {
      this.version = newVersion;
    }

    public String login() {
      return login;
    }

    public char[] password() {
      return password;
    }
  }
}
