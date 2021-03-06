package arollavengers.core.domain.pandemic;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;
import java.util.EnumSet;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PlayerCityCardTest {

    @Test
    public void each_city_has_its_own_card() {
        EnumSet<CityId> cityIds = EnumSet.allOf(CityId.class);
        for(PlayerCityCard card : PlayerCityCard.values()) {
            assertThat(cityIds).contains(card.cityId());
            assertThat(cityIds.remove(card.cityId())).isTrue();
        }

        assertThat(cityIds).isEmpty();
    }
}
