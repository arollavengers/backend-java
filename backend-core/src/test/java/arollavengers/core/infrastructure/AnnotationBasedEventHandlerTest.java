package arollavengers.core.infrastructure;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class AnnotationBasedEventHandlerTest {
    @Test
    public void failOnMissing_is_true_by_default() throws Exception {
        assertThat(new AnnotationBasedEventHandler<Object>("").isFailOnMissing()).isTrue();
    }
}
