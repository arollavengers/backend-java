package arollavengers.core.testutils;

import arollavengers.core.infrastructure.DomainEvent;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.JacksonSerializer;
import arollavengers.core.infrastructure.eventstore.EventStoreInMemory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class EventStoreInMemoryWriter {
    private File basedir;
    public EventStoreInMemoryWriter(String basedir) {
        this.basedir = new File(basedir);
    }

    public void write(EventStoreInMemory eventStore) throws IOException {
        if(!basedir.exists() && !basedir.mkdirs())
            throw new IOException("Failed to create directory at " + basedir.getAbsolutePath());

        JacksonSerializer serializer = new JacksonSerializer();
        serializer.postConstruct();

        ConcurrentMap<Id,List<DomainEvent>> eventsPerStream = eventStore.getEventsPerStream();
        for (Map.Entry<Id,List<DomainEvent>> stream : eventsPerStream.entrySet()) {
            FileOutputStream streamOut = new FileOutputStream(new File(basedir, stream.getKey().asString()));
            List<DomainEvent> events = stream.getValue();
            serializer.writeObject(streamOut, events.toArray(new DomainEvent[events.size()]));
            streamOut.close();
        }
    }
}
