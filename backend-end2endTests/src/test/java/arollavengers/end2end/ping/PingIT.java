package arollavengers.end2end.ping;

import static org.fest.assertions.api.Assertions.assertThat;

import arollavengers.end2end.TestSettings;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PingIT {

    private DefaultHttpClient httpclient;
    private TestSettings settings;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws IOException {
        httpclient = new DefaultHttpClient();
        settings = new TestSettings();
        mapper = new ObjectMapper();

    }

    @Test
    public void pong() throws IOException {
        HttpGet httpGet = new HttpGet(settings.getBaseURL() + "/rest/ping/Travis");

        long before = System.currentTimeMillis();
        HttpResponse response = httpclient.execute(httpGet);
        long after = System.currentTimeMillis();

        // The underlying HTTP connection is still held by the response object
        // to allow the response content to be streamed directly from the network socket.
        // In order to ensure correct deallocation of system resources
        // the user MUST either fully consume the response content  or abort request
        // execution by calling HttpGet#releaseConnection().

        try {
            assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);

            HttpEntity entity = response.getEntity();
            InputStream entityContent = entity.getContent();
            JsonNode node = mapper.readTree(entityContent);

            assertThat(node.get("name").asText()).isEqualTo("Travis");
            assertThat(node.get("timeMillis").asLong()).isGreaterThanOrEqualTo(before).isLessThanOrEqualTo(after);
            //ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            //System.out.println(writer.writeValueAsString(node));

            EntityUtils.consume(entity);
        }
        finally {
            httpGet.releaseConnection();
        }
    }
}
