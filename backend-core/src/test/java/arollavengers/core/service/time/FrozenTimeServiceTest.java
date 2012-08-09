package arollavengers.core.service.time;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FrozenTimeServiceTest {

    private FrozenTimeService service;

    @Before
    public void setUp () {
        service = new FrozenTimeService();
    }

    @Test
    public void timeIsFrozen_atZeroByDefault() {
        assertThat(service.currentTimeMillis()).isEqualTo(0L);
    }

    @Test
    public void timeIsFrozen() {
        service.freezeTimeTo(17L);
        assertThat(service.currentTimeMillis()).isEqualTo(17L);
        service.freezeTimeTo(23L);
        assertThat(service.currentTimeMillis()).isEqualTo(23L);
    }
}
