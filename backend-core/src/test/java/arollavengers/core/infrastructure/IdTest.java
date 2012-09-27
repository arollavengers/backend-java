package arollavengers.core.infrastructure;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class IdTest {

    @Test
    public void equals() {
        Id.setDefaultStrategy(Id.Strategy.Uuid);
        assertThat(Id.undefined().equals(Id.undefined())).isTrue();
        assertThat(Id.undefined().equals(Id.next(null))).isFalse();
        assertThat(Id.next(null).equals(Id.next(null))).isFalse();
    }
}
