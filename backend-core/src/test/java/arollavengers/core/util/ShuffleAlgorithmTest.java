package arollavengers.core.util;

import static org.fest.assertions.api.Assertions.assertThat;

import com.google.common.collect.Lists;

import org.junit.Test;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ShuffleAlgorithmTest {

    @Test
    public void GSR_case1 () {
        List<String> cards1 = generateCards(20);
        List<String> cards2 = generateCards(20);
        ShuffleAlgorithm.GSR.shuffle(cards1);
        ShuffleAlgorithm.GSR.shuffle(cards2);

        assertThat(cards1).isNotEqualTo(cards2);
    }

    private static List<String> generateCards(int amount) {
        List<String> cards = Lists.newArrayList();
        for(int i=0;i<amount;i++) {
            cards.add(String.valueOf(i));
        }
        return cards;
    }
}
