package arollavengers.core.domain.pandemic;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class InfectionRateTest {
    @Test
    public void next() {
        assertThat(InfectionRate.first.next()).isEqualTo(InfectionRate.second);
        assertThat(InfectionRate.second.next()).isEqualTo(InfectionRate.third);
        assertThat(InfectionRate.third.next()).isEqualTo(InfectionRate.fourth);
        assertThat(InfectionRate.fourth.next()).isEqualTo(InfectionRate.fifth);
        assertThat(InfectionRate.fifth.next()).isEqualTo(InfectionRate.sixth);
        assertThat(InfectionRate.sixth.next()).isEqualTo(InfectionRate.seventh);
        assertThat(InfectionRate.seventh.next()).isEqualTo(InfectionRate.seventh);
    }
}
