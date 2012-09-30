package arollavengers.core.events;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PolymorphicEventMixInTest {
    @Test
    public void testCollectAllClasses() throws Exception {
        System.out.println("PolymorphicEventMixInTest.testCollectAllClasses[\n" +
                StringUtils.join(PolymorphicEventMixIn.collectAllEventClasses(), "\n"));
    }
}
