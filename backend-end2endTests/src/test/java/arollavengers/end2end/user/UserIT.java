package arollavengers.end2end.user;

import static org.fest.assertions.api.Assertions.assertThat;

import arollavengers.end2end.TestSettings;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class UserIT {

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
    public void createUser () throws IOException {
        String json = "{ 'login':'Travis', 'password':'sivarT' }".replace('\'', '"');

        HttpPost httpPost = new HttpPost(settings.getBaseURL() + "/api/users");
        httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

        HttpResponse response = httpclient.execute(httpPost);
        assertThat(response.getStatusLine().getStatusCode())
                .describedAs(response.getStatusLine().toString())
                .isEqualTo(200);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println("UserIT.createUser( ==> " + responseBody + ")");
    }

}
