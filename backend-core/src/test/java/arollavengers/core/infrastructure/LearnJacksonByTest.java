package arollavengers.core.infrastructure;

import static org.fest.assertions.api.Assertions.assertThat;

import com.google.common.collect.Maps;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.deser.std.StdDeserializer;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
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

    @Test
    public void deserialize_event_programaticMixin() throws IOException {
        String event = "{\n" +
                "  \"type\" : \"create\",\n" +
                "  \"version\" : 0,\n" +
                "  \"id\" : \"4241febf-4901-43b6-b8ed-c9a75770fbb6\",\n" +
                "  \"login\" : \"Travis\",\n" +
                "  \"password\" : \"Pacman\"\n" +
                "}";

        DomainEventDeserializer eventDeserializer = new DomainEventDeserializer();
        eventDeserializer.registerEvent("create", CreateEvent.class);
        eventDeserializer.registerEvent("modify", ModifyEvent.class);

        SimpleModule module = new SimpleModule("events", new Version(1, 0, 0, null));
        module.addDeserializer(DomainEvent.class, eventDeserializer);
        //module.addSerializer(DomainEvent.class, eventSerializer);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);


        Object o = objectMapper.readValue(event, DomainEvent.class);
        assertThat(o).isInstanceOf(CreateEvent.class);
        CreateEvent evt = (CreateEvent) o;
        assertThat(evt.login()).isEqualTo("Travis");
        assertThat(evt.password()).isEqualTo("Pacman".toCharArray());
        assertThat(evt.entityId()).isEqualTo(Id.create("4241febf-4901-43b6-b8ed-c9a75770fbb6"));

        System.out.println("LearnJacksonByTest.deserialize_event_programaticMixin" +
                "\n" + objectMapper.writeValueAsString(evt));

    }

    @Test
    public void deserialize_event_annotationMixin_withName() throws IOException {
        String event = "{\n" +
                "  \"type\" : \"create\",\n" +
                "  \"version\" : 0,\n" +
                "  \"id\" : \"4241febf-4901-43b6-b8ed-c9a75770fbb6\",\n" +
                "  \"login\" : \"Travis\",\n" +
                "  \"password\" : \"Pacman\"\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper
                .getDeserializationConfig()
                .addMixInAnnotations(DomainEvent.class, PolymorphicEventMixIn2.class);
        objectMapper
                .getSerializationConfig()
                .addMixInAnnotations(DomainEvent.class, PolymorphicEventMixIn2.class);

        Object o = objectMapper.readValue(event, DomainEvent.class);
        assertThat(o).isInstanceOf(CreateEvent.class);
        CreateEvent evt = (CreateEvent) o;
        assertThat(evt.login()).isEqualTo("Travis");
        assertThat(evt.password()).isEqualTo("Pacman".toCharArray());
        assertThat(evt.entityId()).isEqualTo(Id.create("4241febf-4901-43b6-b8ed-c9a75770fbb6"));

        System.out.println("LearnJacksonByTest.deserialize_event_annotationMixin_withName" +
                "\n" + objectMapper.writeValueAsString(evt));
    }

    @Test
    public void deserialize_event_annotationMixin_withName_mixin3() throws IOException {
        /*
         * Finally a suitable solution!!
         * thanks to
         * http://stackoverflow.com/questions/8359831/jackson-support-for-polymorphism-without-annotations-and-dedicated-bean-fields#8369237
         *
         */


        String event = "{\n" +
                "  \"type\" : \"create-event\",\n" +
                "  \"version\" : 0,\n" +
                "  \"id\" : \"4241febf-4901-43b6-b8ed-c9a75770fbb6\",\n" +
                "  \"login\" : \"Travis\",\n" +
                "  \"password\" : \"Pacman\"\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setAnnotationIntrospector(new CustomJacksonAnnotationIntrospector());
        objectMapper
                .getDeserializationConfig()
                .addMixInAnnotations(DomainEvent.class, PolymorphicEventMixIn3.class)
        ;
        objectMapper
                .getSerializationConfig()
                .addMixInAnnotations(DomainEvent.class, PolymorphicEventMixIn3.class)
        ;

        Object o = objectMapper.readValue(event, DomainEvent.class);
        assertThat(o).isInstanceOf(CreateEvent.class);
        CreateEvent evt = (CreateEvent) o;
        assertThat(evt.login()).isEqualTo("Travis");
        assertThat(evt.password()).isEqualTo("Pacman".toCharArray());
        assertThat(evt.entityId()).isEqualTo(Id.create("4241febf-4901-43b6-b8ed-c9a75770fbb6"));

        System.out.println("LearnJacksonByTest.deserialize_event_annotationMixin_withName" +
                "\n" + objectMapper.writeValueAsString(evt));
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

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(CreateEvent.class),
            @JsonSubTypes.Type(ModifyEvent.class)
    })
    abstract class PolymorphicEventMixIn2 {
    }

    // --------------------------------------------------------------------------

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type")
    abstract class PolymorphicEventMixIn3 {
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

    public static abstract class AbstractDomainEvent implements DomainEvent {
        protected static final String ENTITY_ID_KEY = "id";

        @JsonProperty
        private long version;

        @JsonProperty(ENTITY_ID_KEY)
        private final Id entityId;

        protected AbstractDomainEvent(Id entityId) {
            this.entityId = entityId;
        }

        @Override
        public long version() {
            return version;
        }

        @Override
        public Id entityId() {
            return entityId;
        }

        @Override
        public void assignVersion(long newVersion) {
            this.version = newVersion;
        }
    }

    @JsonTypeName("create")
    public static class CreateEvent extends AbstractDomainEvent {

        @JsonProperty
        private final String login;
        @JsonProperty
        private final char[] password;

        @JsonCreator
        public CreateEvent(@JsonProperty(ENTITY_ID_KEY) Id id,
                           @JsonProperty("login") String login,
                           @JsonProperty("password") char[] password)
        {
            super(id);
            this.login = login;
            this.password = password;
        }

        public String login() {
            return login;
        }

        public char[] password() {
            return password;
        }
    }

    @JsonTypeName("modify")
    public static class ModifyEvent extends AbstractDomainEvent {
        private final String login;

        @JsonCreator
        public ModifyEvent(@JsonProperty(ENTITY_ID_KEY) Id id,
                           @JsonProperty("login") String login)
        {
            super(id);
            this.login = login;
        }

        public String login() {
            return login;
        }
    }

    /**
     *
     *
     *
     *
     *
     */
    public static class DomainEventDeserializer extends StdDeserializer<DomainEvent> {
        private static final Logger log = LoggerFactory.getLogger(DomainEventDeserializer.class);

        private Map<String, Class<? extends DomainEvent>> registry = Maps.newHashMap();

        public DomainEventDeserializer() {
            super(DomainEvent.class);
        }

        public void registerEvent(String uniqueAttribute,
                                  Class<? extends DomainEvent> domainEventClass)
        {
            Class<? extends DomainEvent> previous = registry.put(uniqueAttribute, domainEventClass);
            if (previous != null) {
                log.warn("Type {} is potentially masked by {} using name {}",
                        new Object[]{previous, domainEventClass, uniqueAttribute});
            }
        }

        @Override
        public DomainEvent deserialize(
                JsonParser jp, DeserializationContext ctxt)
                throws IOException
        {
            ObjectMapper mapper = (ObjectMapper) jp.getCodec();
            ObjectNode root = (ObjectNode) mapper.readTree(jp);

            if (log.isTraceEnabled()) {
                ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
                StringWriter writer = new StringWriter();
                objectWriter.writeValue(writer, root);
                log.trace("Content to deserialize: {}", writer);
            }

            // remove type info otherwise it cannot be matched to concrete class field
            JsonNode removed = root.remove("type");
            if (removed == null) {
                throw new Serializer.SerializationException("No type information");
            }

            Class<? extends DomainEvent> domainEventClass = registry.get(removed.getTextValue());
            if (domainEventClass == null) {
                return null;
            }
            return mapper.readValue(root, domainEventClass);
        }
    }

    /**
     *
     */
    public static class CustomJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {
        @Override
        public List<NamedType> findSubtypes(Annotated a) {
            System.out.println(
                    "LearnJacksonByTest$CustomJacksonAnnotationIntrospector.findSubtypes(" + a.getRawType() + ")");
            if (a.getRawType().equals(DomainEvent.class)) {
                return Arrays.asList(
                        new NamedType(CreateEvent.class),
                        new NamedType(ModifyEvent.class));
            }
            return super.findSubtypes(a);
        }

        @Override
        public String findTypeName(AnnotatedClass ac) {
            System.out.println(
                    "LearnJacksonByTest$CustomJacksonAnnotationIntrospector.findTypeName(" + ac.getRawType() + ")");
            return separate(ac.getRawType().getSimpleName(), '-');
//            return super.findTypeName(ac);
        }

    }

    /**
     * Converts a camelCase string to a separated one. Receives the separator character
     *
     * @param text      the text to transform
     * @param separator the separator character to use
     * @return the separated text.
     */
    public static String separate(String text, char separator) {
        StringBuilder builder = new StringBuilder(text.length() + text.length() / 2);
        int l = text.length();
        for (int i = 0; i < l; i++) {
            char c = text.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i != 0) {
                    builder.append(separator);
                }
                builder.append(Character.toLowerCase(c));
            }
            else {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
