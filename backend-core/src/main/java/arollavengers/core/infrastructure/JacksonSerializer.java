package arollavengers.core.infrastructure;

import arollavengers.core.events.PolymorphicEventMixIn;
import arollavengers.core.util.jackson.JacksonAnnotationIntrospectorCustom;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class JacksonSerializer implements Serializer {
    private static final Logger log = LoggerFactory.getLogger(JacksonSerializer.class);

    private ObjectWriter objectWriter;
    private ObjectMapper deserializer;

    @PostConstruct
    public void postConstruct() {

        JacksonAnnotationIntrospectorCustom introspector = new JacksonAnnotationIntrospectorCustom();

        // typing (type info in json): special management for all DomainEvent
        introspector.registerSubTypes(DomainEvent.class, PolymorphicEventMixIn.collectAllEventClasses());


        ObjectMapper objectMapper = new ObjectMapper()
                // default typing for all other except 'DomainEvent' and those that
                // defines @JsonTypeInfo/@JsonTypeName/@JsonSubTypes
                // e.g. InfectionCard/InfectionCityCard
                .enableDefaultTypingAsProperty(
                        ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE,
                        "@class");
        objectMapper.setAnnotationIntrospector(introspector);
        objectMapper
                .getDeserializationConfig()
                .addMixInAnnotations(DomainEvent.class, PolymorphicEventMixIn.class)
        ;
        objectMapper
                .getSerializationConfig()
                .addMixInAnnotations(DomainEvent.class, PolymorphicEventMixIn.class)
        ;
        objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        deserializer = objectMapper;
    }

    @Override
    public void writeObject(OutputStream outputStream, Object object) throws SerializationException {
        OutputStream out = outputStream;
        if (log.isTraceEnabled()) {
            out = new ByteArrayOutputStream();
        }
        try {
            objectWriter.writeValue(out, object);

            if (log.isTraceEnabled()) {
                ByteArrayOutputStream bout = (ByteArrayOutputStream) out;
                log.trace("Serialized content: {}", bout.toString("utf-8"));
                outputStream.write(bout.toByteArray());
            }
        }
        catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public String serializeAsString(Object event) {
        try {
            return objectWriter.writeValueAsString(event);
        }
        catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public Object readObject(InputStream inputStream, Class<?> type) throws SerializationException {
        try {
            return deserializer.readValue(inputStream, type);
        }
        catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public Object deserializeFomString(String json, Class<?> type) {
        try {
            return deserializer.readValue(json, type);
        }
        catch (IOException e) {
            throw new SerializationException(e);
        }
    }

}
