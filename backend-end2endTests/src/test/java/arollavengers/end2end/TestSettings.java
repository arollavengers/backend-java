package arollavengers.end2end;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Ignore
public class TestSettings {
    private Properties properties;

    public TestSettings() throws IOException {
        properties = new Properties();

        InputStream inputStream = TestSettings.class.getResourceAsStream("/test-settings.properties");
        try {
            properties.load(inputStream);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public String getBaseURL () {
        return properties.getProperty("baseURL");
    }
}
