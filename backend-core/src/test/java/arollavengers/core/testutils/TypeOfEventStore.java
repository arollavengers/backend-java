package arollavengers.core.testutils;

import arollavengers.core.TestSettings;
import arollavengers.core.infrastructure.EventStore;
import arollavengers.core.infrastructure.JacksonSerializer;
import arollavengers.core.infrastructure.eventstore.EventStoreInMemory;
import arollavengers.core.infrastructure.eventstore.EventStoreJdbc;
import arollavengers.core.infrastructure.eventstore.EventStorePrevayler;

import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import java.io.FileInputStream;
import java.sql.Driver;
import java.util.UUID;

/**
* @author <a href="http://twitter.com/aloyer">@aloyer</a>
*/
public enum TypeOfEventStore {
    InMemory
            {
                @Override
                public EventStore eventStore(TestSettings testSettings) {
                    return new EventStoreInMemory();
                }
            },
    Prevayler
            {
                @Override
                public EventStore eventStore(TestSettings testSettings) throws Exception {
                    String dataFolder =
                            testSettings.getProperty("prevayler.event-store.basedir") + "/"
                                    + UUID.randomUUID().toString();

                    JacksonSerializer serializer = new JacksonSerializer();
                    serializer.postConstruct();

                    EventStorePrevayler eventStore = new EventStorePrevayler();
                    eventStore.setDataFolder(dataFolder);
                    eventStore.setSerializer(serializer);
                    eventStore.postConstruct();
                    return eventStore;
                }
            },
    Jdbc
            {
                @SuppressWarnings("unchecked")
                @Override
                public EventStore eventStore(TestSettings testSettings) throws Exception {
                    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
                    dataSource.setDriverClass((Class<? extends Driver>) Class.forName(testSettings.getProperty("jdbc.driver")));
                    dataSource.setUrl(testSettings.getProperty("jdbc.url"));
                    for (String script : testSettings.getProperty("jdbc.scripts").split(",")) {
                        FileInputStream inputStream = new FileInputStream(script.trim());
                        try {
                            SqlUtils.executeScriptOn(dataSource, IOUtils.toString(inputStream, "utf-8"));
                        }
                        finally {
                            IOUtils.closeQuietly(inputStream);
                        }
                    }

                    JacksonSerializer serialier = new JacksonSerializer();
                    serialier.postConstruct();

                    EventStoreJdbc eventStore = new EventStoreJdbc();
                    eventStore.setDataSource(dataSource);
                    eventStore.setSerializer(serialier);
                    eventStore.postConstruct();
                    return eventStore;
                }
            };
    //Hibernate

    public abstract EventStore eventStore(TestSettings testSettings) throws Exception;

}
