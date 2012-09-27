package arollavengers.core.domain.pandemic;

import static arollavengers.core.domain.pandemic.CityId.Algiers;
import static arollavengers.core.domain.pandemic.CityId.Atlanta;
import static arollavengers.core.domain.pandemic.CityId.Baghdad;
import static arollavengers.core.domain.pandemic.CityId.Bangkok;
import static arollavengers.core.domain.pandemic.CityId.Cairo;
import static arollavengers.core.domain.pandemic.CityId.Chennai;
import static arollavengers.core.domain.pandemic.CityId.Chicago;
import static arollavengers.core.domain.pandemic.CityId.Delhi;
import static arollavengers.core.domain.pandemic.CityId.Essen;
import static arollavengers.core.domain.pandemic.CityId.HongKong;
import static arollavengers.core.domain.pandemic.CityId.Istanbul;
import static arollavengers.core.domain.pandemic.CityId.Jakarta;
import static arollavengers.core.domain.pandemic.CityId.Karachi;
import static arollavengers.core.domain.pandemic.CityId.Khartoum;
import static arollavengers.core.domain.pandemic.CityId.Kolkata;
import static arollavengers.core.domain.pandemic.CityId.London;
import static arollavengers.core.domain.pandemic.CityId.LosAngeles;
import static arollavengers.core.domain.pandemic.CityId.Madrid;
import static arollavengers.core.domain.pandemic.CityId.Manila;
import static arollavengers.core.domain.pandemic.CityId.MexicoCity;
import static arollavengers.core.domain.pandemic.CityId.Miami;
import static arollavengers.core.domain.pandemic.CityId.Milan;
import static arollavengers.core.domain.pandemic.CityId.Moscow;
import static arollavengers.core.domain.pandemic.CityId.Mumbai;
import static arollavengers.core.domain.pandemic.CityId.NewYork;
import static arollavengers.core.domain.pandemic.CityId.Paris;
import static arollavengers.core.domain.pandemic.CityId.Riyadh;
import static arollavengers.core.domain.pandemic.CityId.SaintPetersburg;
import static arollavengers.core.domain.pandemic.CityId.SanFrancisco;
import static arollavengers.core.domain.pandemic.CityId.SaoPaulo;
import static arollavengers.core.domain.pandemic.CityId.Tehran;
import static arollavengers.core.domain.pandemic.CityId.Tokyo;
import static arollavengers.core.domain.pandemic.CityId.Toronto;
import static arollavengers.core.domain.pandemic.CityId.Washington;
import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CityGraphTest {

    private CityGraph cityGraph;

    @Before
    public void setUp () {
        cityGraph = CityGraph.getInstance().initializeDefaultLinks();
    }

    @Test
    public void adjacentCitiesOf_() {
        assertThat(cityGraph.adjacentCitiesOf(Chicago)).containsOnly(SanFrancisco, LosAngeles, MexicoCity, Atlanta, Toronto);
        assertThat(cityGraph.adjacentCitiesOf(SaintPetersburg)).containsOnly(Moscow, Istanbul, Essen);
        assertThat(cityGraph.adjacentCitiesOf(Moscow)).containsOnly(SaintPetersburg, Istanbul, Tehran);
    }
    
    @Test
    public void areAdjacents_blue() {
        // ~~~ Blue
        assertCitiesAreAdjacent(SanFrancisco, Tokyo);
        assertCitiesAreAdjacent(SanFrancisco, Manila);
        assertCitiesAreAdjacent(SanFrancisco, LosAngeles);
        assertCitiesAreAdjacent(SanFrancisco, Chicago);
        assertCitiesAreAdjacent(Chicago, SanFrancisco);
        assertCitiesAreAdjacent(Chicago, LosAngeles);
        assertCitiesAreAdjacent(Chicago, MexicoCity);
        assertCitiesAreAdjacent(Chicago, Atlanta);
        assertCitiesAreAdjacent(Chicago, Toronto);
        assertCitiesAreAdjacent(Atlanta, Chicago);
        assertCitiesAreAdjacent(Atlanta, Miami);
        assertCitiesAreAdjacent(Atlanta, Washington);
        assertCitiesAreAdjacent(Toronto, Chicago);
        assertCitiesAreAdjacent(Toronto, Washington);
        assertCitiesAreAdjacent(Toronto, NewYork);
        assertCitiesAreAdjacent(Washington, NewYork);
        assertCitiesAreAdjacent(Washington, Toronto);
        assertCitiesAreAdjacent(Washington, Atlanta);
        assertCitiesAreAdjacent(Washington, Miami);
        assertCitiesAreAdjacent(NewYork, Toronto);
        assertCitiesAreAdjacent(NewYork, Washington);
        assertCitiesAreAdjacent(NewYork, Madrid);
        assertCitiesAreAdjacent(NewYork, London);
        assertCitiesAreAdjacent(Madrid, NewYork);
        assertCitiesAreAdjacent(Madrid, SaoPaulo);
        assertCitiesAreAdjacent(Madrid, Algiers);
        assertCitiesAreAdjacent(Madrid, Paris);
        assertCitiesAreAdjacent(Madrid, London);
        assertCitiesAreAdjacent(London, NewYork);
        assertCitiesAreAdjacent(London, Madrid);
        assertCitiesAreAdjacent(London, Paris);
        assertCitiesAreAdjacent(London, Essen);
        assertCitiesAreAdjacent(Paris, London);
        assertCitiesAreAdjacent(Paris, Madrid);
        assertCitiesAreAdjacent(Paris, Algiers);
        assertCitiesAreAdjacent(Paris, Milan);
        assertCitiesAreAdjacent(Paris, Essen);
        assertCitiesAreAdjacent(Essen, London);
        assertCitiesAreAdjacent(Essen, Paris);
        assertCitiesAreAdjacent(Essen, Milan);
        assertCitiesAreAdjacent(Essen, SaintPetersburg);
        assertCitiesAreAdjacent(Milan, Essen);
        assertCitiesAreAdjacent(Milan, Paris);
        assertCitiesAreAdjacent(Milan, Istanbul);
        assertCitiesAreAdjacent(SaintPetersburg, Essen);
        assertCitiesAreAdjacent(SaintPetersburg, Istanbul);
        assertCitiesAreAdjacent(SaintPetersburg, Moscow);
    }

    @Test
    public void areAdjacents_black() {
        // ~~~ Black
        assertCitiesAreAdjacent(Algiers, Madrid);
        assertCitiesAreAdjacent(Algiers, Paris);
        assertCitiesAreAdjacent(Algiers, Istanbul);
        assertCitiesAreAdjacent(Algiers, Cairo);
        assertCitiesAreAdjacent(Istanbul, Algiers);
        assertCitiesAreAdjacent(Istanbul, Milan);
        assertCitiesAreAdjacent(Istanbul, SaintPetersburg);
        assertCitiesAreAdjacent(Istanbul, Moscow);
        assertCitiesAreAdjacent(Istanbul, Baghdad);
        assertCitiesAreAdjacent(Istanbul, Cairo);
        assertCitiesAreAdjacent(Moscow, SaintPetersburg);
        assertCitiesAreAdjacent(Moscow, Tehran);
        assertCitiesAreAdjacent(Moscow, Istanbul);
        assertCitiesAreAdjacent(Tehran, Moscow);
        assertCitiesAreAdjacent(Tehran, Baghdad);
        assertCitiesAreAdjacent(Tehran, Karachi);
        assertCitiesAreAdjacent(Tehran, Delhi);
        assertCitiesAreAdjacent(Delhi, Tehran);
        assertCitiesAreAdjacent(Delhi, Karachi);
        assertCitiesAreAdjacent(Delhi, Mumbai);
        assertCitiesAreAdjacent(Delhi, Chennai);
        assertCitiesAreAdjacent(Delhi, Kolkata);
        assertCitiesAreAdjacent(Kolkata, Delhi);
        assertCitiesAreAdjacent(Kolkata, HongKong);
        assertCitiesAreAdjacent(Kolkata, Bangkok);
        assertCitiesAreAdjacent(Kolkata, Chennai);
        assertCitiesAreAdjacent(Chennai, Kolkata);
        assertCitiesAreAdjacent(Chennai, Bangkok);
        assertCitiesAreAdjacent(Chennai, Jakarta);
        assertCitiesAreAdjacent(Chennai, Mumbai);
        assertCitiesAreAdjacent(Chennai, Delhi);
        assertCitiesAreAdjacent(Mumbai, Karachi);
        assertCitiesAreAdjacent(Mumbai, Delhi);
        assertCitiesAreAdjacent(Mumbai, Chennai);
        assertCitiesAreAdjacent(Karachi, Baghdad);
        assertCitiesAreAdjacent(Karachi, Tehran);
        assertCitiesAreAdjacent(Karachi, Delhi);
        assertCitiesAreAdjacent(Karachi, Mumbai);
        assertCitiesAreAdjacent(Karachi, Riyadh);
        assertCitiesAreAdjacent(Baghdad, Istanbul);
        assertCitiesAreAdjacent(Baghdad, Tehran);
        assertCitiesAreAdjacent(Baghdad, Karachi);
        assertCitiesAreAdjacent(Baghdad, Riyadh);
        assertCitiesAreAdjacent(Baghdad, Cairo);
        assertCitiesAreAdjacent(Cairo, Algiers);
        assertCitiesAreAdjacent(Cairo, Istanbul);
        assertCitiesAreAdjacent(Cairo, Baghdad);
        assertCitiesAreAdjacent(Cairo, Riyadh);
        assertCitiesAreAdjacent(Cairo, Khartoum);
        assertCitiesAreAdjacent(Riyadh, Cairo);
        assertCitiesAreAdjacent(Riyadh, Baghdad);
        assertCitiesAreAdjacent(Riyadh, Karachi);
    }

    private void assertCitiesAreAdjacent(CityId one, CityId two) {
        assertThat(cityGraph.areAdjacent(one, two)).isTrue();
    }
}
