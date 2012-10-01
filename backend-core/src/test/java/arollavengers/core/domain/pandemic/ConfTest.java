package arollavengers.core.domain.pandemic;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ConfTest {
    @Test
    public void defaults() throws Exception {
        Conf conf = Conf.getDefault();
        assertThat(conf.maxPlayerHandSize()).isEqualTo(7);
        assertThat(conf.minTeamSize()).isEqualTo(2);
        assertThat(conf.maxTeamSize()).isEqualTo(4);
        assertThat(conf.nbCubesOutbreakThreshold()).isEqualTo(4);
        assertThat(conf.nbPlayerActionsPerTurn()).isEqualTo(4);
        assertThat(conf.nbPlayerCardsPerTurn()).isEqualTo(2);
    }
}
