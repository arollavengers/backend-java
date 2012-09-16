package arollavengers.core;

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
    InputStream inStream = getClass().getResourceAsStream("/settings-test.properties");
    try {
      properties.load(inStream);
    }
    finally {
      if(inStream!=null)
        inStream.close();
    }
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }

  public String getProperty(String key, String defaultValue) {
    return properties.getProperty(key, defaultValue);
  }
}
