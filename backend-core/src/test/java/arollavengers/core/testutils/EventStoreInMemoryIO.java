package arollavengers.core.testutils;

import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.JacksonSerializer;
import arollavengers.core.infrastructure.VersionedDomainEvent;
import arollavengers.core.infrastructure.eventstore.EventStoreInMemory;

import org.apache.commons.lang3.StringUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class EventStoreInMemoryIO {
    private File basedir;

    public EventStoreInMemoryIO(String basedir) {
        this.basedir = new File(basedir);
    }

    public void write(EventStoreInMemory eventStore) throws IOException {
        if (!basedir.exists() && !basedir.mkdirs()) {
            throw new IOException("Failed to create directory at " + basedir.getAbsolutePath());
        }

        JacksonSerializer serializer = new JacksonSerializer();
        serializer.postConstruct();

        ConcurrentMap<Id, List<VersionedDomainEvent>> eventsPerStream = eventStore.getEventsPerStream();
        for (Map.Entry<Id, List<VersionedDomainEvent>> stream : eventsPerStream.entrySet()) {
            FileOutputStream streamOut = new FileOutputStream(new File(basedir, stream.getKey().asString() + ".evt"));
            List<VersionedDomainEvent> events = stream.getValue();
            serializer.writeObject(streamOut, events.toArray(new VersionedDomainEvent[events.size()]));
            streamOut.close();
        }
    }

    public void load(EventStoreInMemory eventStore) throws IOException {
        if (!basedir.exists()) {
            throw new IOException("No directory at " + basedir.getAbsolutePath());
        }

        JacksonSerializer serializer = new JacksonSerializer();
        serializer.postConstruct();

        ConcurrentMap<Id, List<VersionedDomainEvent>> eventsPerStream = eventStore.getEventsPerStream();
        for (File entityFile : basedir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".evt");
            }
        }))
        {
            String entityId = StringUtils.remove(entityFile.getName(), ".evt");
            FileInputStream streamIn = new FileInputStream(entityFile);
            VersionedDomainEvent[] events = (VersionedDomainEvent[]) serializer.readObject(streamIn, VersionedDomainEvent[].class);
            eventsPerStream.put(Id.create(entityId), Arrays.asList(events));
        }
    }

}
