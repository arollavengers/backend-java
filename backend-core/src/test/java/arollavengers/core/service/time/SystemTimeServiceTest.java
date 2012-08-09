package arollavengers.core.service.time;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SystemTimeServiceTest {

    private SystemTimeService service;

    @Before
    public void setUp () {
        service = new SystemTimeService();
    }

    @Test
    public void currentTimeMillis() {
        long before = System.currentTimeMillis();
        long serviceTime = service.currentTimeMillis();
        long after = System.currentTimeMillis();
        assertThat(serviceTime).isGreaterThanOrEqualTo(before).isLessThanOrEqualTo(after);
    }
}
