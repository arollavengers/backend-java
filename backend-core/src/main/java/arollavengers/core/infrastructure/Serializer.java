package arollavengers.core.infrastructure;

import arollavengers.core.exceptions.InfrastructureRuntimeException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface Serializer {

    String serializeAsString(Object object) throws SerializationException;

    Object deserializeFomString(String serialized) throws SerializationException;


    Object readObject(InputStream inputStream) throws SerializationException;

    void writeObject(OutputStream outputStream, Object object) throws SerializationException;


    public static class SerializationException extends InfrastructureRuntimeException {
        public SerializationException() {
            super();
        }

        public SerializationException(String message) {
            super(message);
        }

        public SerializationException(String message, Throwable cause) {
            super(message, cause);
        }

        public SerializationException(Throwable cause) {
            super(cause);
        }
    }
}
