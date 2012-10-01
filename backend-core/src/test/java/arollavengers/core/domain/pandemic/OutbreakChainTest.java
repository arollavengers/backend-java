package arollavengers.core.domain.pandemic;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import java.util.EnumMap;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OutbreakChainTest {

    private EnumMap<CityId, Integer> cubePerCities;
    private CityStatesView cityStatesView;
    private CityGraph cityGraph;

    @Before
    public void setUp() {
        cubePerCities = new EnumMap<CityId, Integer>(CityId.class);
        cityStatesView = createViewBackedBy(cubePerCities);
        cityGraph = CityGraph.getInstance();
    }

    //    Toronto-----N.Y------------London------Essen---SaintPetersbourg
    //         \      / \               | \       /  |
    //      Washington   \             /   `-Paris    \
    //                    `-----Madrid------/   | `--Milan
    //                          /    \          |
    //                  SaoPaulo      `-------Algiers

    @Test
    public void reactions_in_chain() {
        cubePerCities.put(CityId.NewYork, 1);
        cubePerCities.put(CityId.London, 3);
        cubePerCities.put(CityId.Madrid, 3);
        cubePerCities.put(CityId.Essen, 2);
        cubePerCities.put(CityId.Paris, 2);

        OutbreakChain chain = OutbreakChain.calculate(CityId.London, Disease.Blue, cityGraph, cityStatesView, Conf.getDefault());
        assertThat(chain).isNotNull();
        assertThat(chain.getOutbreakedCities()).containsOnly(CityId.London, CityId.Madrid, CityId.Essen, CityId.Paris);

        EnumMap<CityId, Integer> resultingInfections = chain.getResultingInfections();
        assertThat(resultingInfections.get(CityId.NewYork)).isEqualTo(3);
        assertThat(resultingInfections.get(CityId.London)).isEqualTo(3);
        assertThat(resultingInfections.get(CityId.Madrid)).isEqualTo(3);
        assertThat(resultingInfections.get(CityId.SaoPaulo)).isEqualTo(1);
        assertThat(resultingInfections.get(CityId.Essen)).isEqualTo(3);
        assertThat(resultingInfections.get(CityId.Paris)).isEqualTo(3);
        assertThat(resultingInfections.get(CityId.Algiers)).isEqualTo(2);
        assertThat(resultingInfections.get(CityId.Milan)).isEqualTo(2);
        assertThat(resultingInfections.get(CityId.SaintPetersburg)).isEqualTo(1);
    }

    private static CityStatesView createViewBackedBy(final EnumMap<CityId, Integer> cubePerCities) {
        return new CityStatesView() {
            @Override
            public CityStateView specializesViewFor(final CityId cityId) {
                return new CityStateView() {
                    @Override
                    public int numberOfCubes(Disease disease) {
                        if(cubePerCities.containsKey(cityId))
                            return cubePerCities.get(cityId);
                        else
                            return 0;
                    }

                    @Override
                    public boolean hasResearchCenter() {
                        return false;
                    }
                };
            }
        };
    }



}
