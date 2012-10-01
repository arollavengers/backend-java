package arollavengers.core.domain.pandemic;

import java.util.EnumMap;
import java.util.EnumSet;

/**
 * <p>An outbreak occurs if a player is required to add a cube to a city that already has 3 cubes in it
 * of that color. When this happens, instead of adding a 4th cube, add a cube of the outbreaking color
 * to each adjacent city.</p>
 *
 * <b>Chain Reactions</b><br/>
 * <p>If any of these new cubes would cause the total number of cubes of that color in an adjacent city
 * to exceed 3, additional outbreaks may occur, causing a chain reaction.</p>
 *
 * <p><em>Note that each city may only outbreak once in each chain reaction.</em></p>
 *
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OutbreakChain {

    public static OutbreakChain calculate(CityId chainStart,
                                          Disease disease,
                                          CityGraph cityGraph,
                                          CityStatesView statesView,
                                          Conf conf)
    {
        OutbreakChain chain = new OutbreakChain(disease, cityGraph, statesView, conf);
        chain.outbreak(chainStart);
        return chain;
    }

    private final Disease disease;
    private final CityGraph cityGraph;
    private final CityStatesView statesView;
    private final Conf conf;
    private final EnumSet<CityId> outbreakedCities;
    private final EnumMap<CityId, Integer> resultingInfections;

    public OutbreakChain(Disease disease,
                         CityGraph cityGraph,
                         CityStatesView statesView,
                         Conf conf) {
        this.disease = disease;
        this.cityGraph = cityGraph;
        this.statesView = statesView;
        this.conf = conf;
        this.outbreakedCities = EnumSet.noneOf(CityId.class);
        this.resultingInfections = new EnumMap<CityId, Integer>(CityId.class);
    }

    public EnumSet<CityId> getOutbreakedCities() {
        return outbreakedCities;
    }

    public EnumMap<CityId, Integer> getResultingInfections() {
        return resultingInfections;
    }

    private void outbreak(CityId cityId) {

        if(outbreakedCities.contains(cityId))
            // Note that each city may only outbreak once in each chain reaction.
            return;
        outbreakedCities.add(cityId);

        for(CityId linked : cityGraph.adjacentCitiesOf(cityId)) {
            defineCityInfectionIfNeeded(linked);

            int nbActualCubes = resultingInfections.get(linked);

            if (nbActualCubes + 1 >= conf.nbCubesOutbreakThreshold()) {
                // recursive booommm !
                outbreak(linked);
            }
            else {
                resultingInfections.put(linked, nbActualCubes+1);
            }
        }
    }

    private void defineCityInfectionIfNeeded(CityId cityId) {
        if(resultingInfections.containsKey(cityId))
            return;
        resultingInfections.put(cityId, statesView.specializesViewFor(cityId).numberOfCubes(disease));
    }

}
