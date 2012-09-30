package arollavengers.core.util.jackson;

import arollavengers.core.infrastructure.Serializer;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.deser.std.StdDeserializer;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.StringWriter;

/**
* @author <a href="http://twitter.com/aloyer">@aloyer</a>
*/
public class TypeAwareDeserializer<E> extends StdDeserializer<E> {


    public static <E> TypeAwareDeserializer<E> typeAwareDeserializer(Class<E> type) {
        return new TypeAwareDeserializer<E>(type);
    }

    private static final Logger log = LoggerFactory.getLogger(TypeAwareDeserializer.class);

    public TypeAwareDeserializer(Class<E> type) {
        super(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public E deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = (ObjectNode) mapper.readTree(jp);

        if (log.isTraceEnabled()) {
            ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
            StringWriter writer = new StringWriter();
            objectWriter.writeValue(writer, root);
            log.trace("Content to deserialize: {}", writer);
        }

        // remove type info otherwise it cannot be matched to concrete class field
        JsonNode removed = root.get("@class");
        if (removed == null) {
            throw new Serializer.SerializationException("No type information");
        }
        String klazzName = removed.getTextValue();

        try {
            Class<?> klazz = Class.forName(klazzName);
            if (!getValueClass().isAssignableFrom(klazz)) {
                throw new Serializer.SerializationException(
                        "Incompatible type information got: " + klazzName + ", expected assignable from: "
                                + getValueClass());
            }
            return (E) mapper.readValue(root, klazz);
        }
        catch (ClassNotFoundException e) {
            throw new Serializer.SerializationException(e);
        }
    }
}
