package arollavengers.core.infrastructure;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.deser.std.StdDeserializer;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.node.ObjectNode;
import org.prevayler.foundation.serialization.Serializer;
import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class JacksonSerializer implements Serializer {

    private ObjectWriter objectWriter;
    private ObjectMapper deserializer;

    @PostConstruct
    public void postConstruct() {

        ObjectMapper mapper =
                new ObjectMapper()
                        .enableDefaultTypingAsProperty(
                                ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE,
                                "@class");
        objectWriter = mapper.writerWithDefaultPrettyPrinter();

        SimpleModule module = new SimpleModule("events", new Version(1, 0, 0, null));
        module.addDeserializer(Object.class, typeAwareDeserializer(Object.class));

        deserializer = new ObjectMapper()
                .enableDefaultTypingAsProperty(
                        ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE,
                        "@class");
        deserializer.registerModule(module);
    }

    @Override
    public void writeObject(OutputStream outputStream, Object object) throws Exception {
        System.out.println("JacksonSerializer.writeObject(" + object + ")");
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        objectWriter.writeValue(bout, object);

        System.out.println("JacksonSerializer.writeObject(" + bout.toString("utf8") + ")");
        outputStream.write(bout.toByteArray());
    }

    public String serializeAsString(Object event) throws IOException {
        return objectWriter.writeValueAsString(event);
    }

    @Override
    public Object readObject(InputStream inputStream) throws Exception {
        return deserializer.readValue(inputStream, Object.class);
    }

    public Object deserializeFomString(String json) throws IOException {
        return deserializer.readValue(json, Object.class);
    }


    public static <E> TypeAwareDeserializer<E> typeAwareDeserializer(Class<E> type) {
        return new TypeAwareDeserializer<E>(type);
    }

    public static class TypeAwareDeserializer<E> extends StdDeserializer<E> {
        protected TypeAwareDeserializer(Class<E> type) {
            super(type);
        }

        @SuppressWarnings("unchecked")
        @Override
        public E deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            ObjectMapper mapper = (ObjectMapper) jp.getCodec();
            ObjectNode root = (ObjectNode) mapper.readTree(jp);


            ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
            StringWriter writer = new StringWriter();
            objectWriter.writeValue(writer, root);
            System.out.println("JacksonSerializer$TypeAwareDeserializer.deserialize: " + writer);

            // remove type info otherwise it cannot be matched to concrete class field
            JsonNode removed = root.get("@class");
            if (removed == null) {
                throw new RuntimeException("No type information");
            }
            String klazzName = removed.getTextValue();

            try {
                Class<?> klazz = Class.forName(klazzName);
                if (!getValueClass().isAssignableFrom(klazz)) {
                    throw new RuntimeException(
                            "Incompatible type information got: " + klazzName + ", expected assignable from: "
                                    + getValueClass());
                }
                return (E) mapper.readValue(root, klazz);
            }
            catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
