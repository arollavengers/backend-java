package arollavengers.core.domain.pandemic;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

/**
 * <p>An outbreak occurs if a player is required to add a cube to a city that already has 3 cubes in it
 * of that color. When this happens, instead of adding a 4th cube, add a cube of the outbreaking color
 * to each adjacent city.</p>
 * <p/>
 * <b>Chain Reactions</b><br/>
 * <p>If any of these new cubes would cause the total number of cubes of that color in an adjacent city
 * to exceed 3, additional outbreakedCities may occur, causing a chain reaction.</p>
 * <p/>
 * <p><em>Note that each city may only outbreak once in each chain reaction.</em></p>
 *
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OutbreakGenerationChain {
    public static final int OUTBREAK_THRESHOLD = 4;

    public static OutbreakGenerationChain calculate(CityId chainStart,
                                                    Disease disease,
                                                    CityGraph cityGraph,
                                                    CityStatesView statesView)
    {
        OutbreakGenerationChain chain = new OutbreakGenerationChain(disease, cityGraph, statesView);
        chain.outbreak(chainStart, 0, null);
        chain.cleanGenerations();
        return chain;
    }

    private final Disease disease;
    private final CityGraph cityGraph;
    private final CityStatesView statesView;
    private final EnumMap<CityId, Integer> resultingInfections;
    private final EnumMap<CityId, OutbreakGeneration> outbreakedCities;
    private final EnumMap<CityId, EnumSet<CityId>> infestorsMap;

    private OutbreakGenerationChain(Disease disease,
                                    CityGraph cityGraph,
                                    CityStatesView statesView)
    {
        this.disease = disease;
        this.cityGraph = cityGraph;
        this.statesView = statesView;
        this.resultingInfections = new EnumMap<CityId, Integer>(CityId.class);
        this.outbreakedCities = new EnumMap<CityId, OutbreakGeneration>(CityId.class);
        this.infestorsMap = new EnumMap<CityId, EnumSet<CityId>>(CityId.class);
    }

    private void cleanGenerations() {
        for (OutbreakGeneration outbreakGeneration : outbreakedCities.values()) {
            Iterator<CityId> iterator = outbreakGeneration.citiesInfected.iterator();
            while (iterator.hasNext()) {
                OutbreakGeneration cityGen = outbreakedCities.get(iterator.next());
                if (cityGen != null && cityGen.generation < outbreakGeneration.generation)
                // city cannot have infect a city of an older generation (lowest generation number)
                {
                    iterator.remove();
                }
            }
        }

    }

    public EnumSet<CityId> getOutbreakedCities() {
        return EnumSet.copyOf(outbreakedCities.keySet());
    }

    public EnumMap<CityId, Integer> getResultingInfections() {
        return resultingInfections;
    }

    public static class OutbreakGenerationMap {
        @JsonProperty
        private final CityId[][] generations;

        @JsonCreator
        public OutbreakGenerationMap(@JsonProperty("generations") CityId[][] generations) {
            this.generations = generations;
        }

        public CityId[] outbreaksForGeneration(int generation) {
            return generations[generation];
        }

        public int numberOfGenerations() {
            return generations.length;
        }

        public int numberOfOutbreaks() {
            int nb = 0;
            for(CityId[] cities : generations) {
                nb += cities.length;
            }
            return nb;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for(int i=0;i<generations.length;i++) {
                builder.append("Generation #").append(i).append("[");
                for(CityId city : generations[i]) {
                    builder.append(city).append(", ");
                }
                builder.setLength(builder.length()-2); // remove last ', '
                builder.append("], ");
            }
            builder.setLength(builder.length()-2); // remove last ', '
            return builder.toString();
        }
    }

    /**
     * Number of generation would be given by <code>multimap.size()</code> whereas the number
     * of outbreaks in the chain would be given by <code>multimap.values().size()</code>.
     */
    public OutbreakGenerationMap toOutbreakGenerationMap() {
        // TODO think of finding a smarter way to not create an intermediate multimap?

        Multimap<Integer, CityId> map = ArrayListMultimap.create();
        for (OutbreakGeneration generation : outbreakedCities.values()) {
            map.put(generation.generation(), generation.cityId);
        }
        CityId[][] generations = new CityId[map.size()][];
        for(int i=0,n=map.size(); i<n; i++) {
            Collection<CityId> cityIds = map.get(i);
            generations[i] = cityIds.toArray(new CityId[cityIds.size()]);
        }

        return new OutbreakGenerationMap(generations);
    }

    public OutbreakGeneration getOutbreakGeneration(CityId cityId) {
        return outbreakedCities.get(cityId);
    }

    private void outbreak(CityId cityId, int generation, CityId causedBy) {
        OutbreakGeneration outbreakGeneration = outbreakedCities.get(cityId);

        // Note that each city may only outbreak once in each chain reaction.
        // one keep only the oldest generation (lowest generation number)
        if (outbreakGeneration != null) {
            if (outbreakGeneration.generation < generation) {
                return;
            }
            else if (outbreakGeneration.generation == generation) {
                if (causedBy != null) {
                    outbreakGeneration.causedBy.add(causedBy);
                }
                return;
            }
        }
        outbreakGeneration = new OutbreakGeneration(generation, cityId);
        if (causedBy != null) {
            outbreakGeneration.causedBy.add(causedBy);
        }
        outbreakedCities.put(cityId, outbreakGeneration);

        for (CityId linked : cityGraph.adjacentCitiesOf(cityId)) {
            defineCityInfectionIfNeeded(linked);
            outbreakGeneration.infects(linked);

            int nbActualCubes = resultingInfections.get(linked);

            if (nbActualCubes + 1 >= OUTBREAK_THRESHOLD) {
                // recursive booommm !
                outbreak(linked, generation + 1, cityId);
            }
            else {
                if (!infestorsOf(linked).contains(cityId)) {
                    infestorsOf(linked).add(cityId);
                    resultingInfections.put(linked, nbActualCubes + 1);
                }
            }
        }
    }

    private EnumSet<CityId> infestorsOf(CityId cityId) {
        EnumSet<CityId> infestors = infestorsMap.get(cityId);
        if (infestors == null) {
            infestors = EnumSet.noneOf(CityId.class);
            infestorsMap.put(cityId, infestors);
        }
        return infestors;
    }

    public static class OutbreakGeneration {
        private final EnumSet<CityId> causedBy;
        private final EnumSet<CityId> citiesInfected;
        private final int generation;
        private final CityId cityId;

        public OutbreakGeneration(int generation, CityId cityId) {
            this.causedBy = EnumSet.noneOf(CityId.class);
            this.citiesInfected = EnumSet.noneOf(CityId.class);
            this.generation = generation;
            this.cityId = cityId;
        }

        void infects(CityId cityInfected) {
            citiesInfected.add(cityInfected);
        }

        /**
         * The cities that have outbreaked and can have created this outbreak as reaction in chain.
         *
         * @return
         */
        public EnumSet<CityId> causedBy() {
            return causedBy;
        }

        /**
         * List of cities this outbreak may have infected.
         *
         * @return
         */
        public EnumSet<CityId> citiesInfected() {
            return citiesInfected;
        }

        /**
         * Generation of the outbreak from a reaction in chain point of view.
         * Lowest numbers are oldest generation, that is the ones that may have cause the others.
         *
         * @return
         */
        public int generation() {
            return generation;
        }

        @Override
        public String toString() {
            return "OutbreakGeneration{" +
                    "generation=" + generation +
                    ", cityId=" + cityId +
                    ", causedBy=" + causedBy +
                    ", citiesInfected=" + citiesInfected +
                    '}';
        }

    }

    private void defineCityInfectionIfNeeded(CityId cityId) {
        if (resultingInfections.containsKey(cityId)) {
            return;
        }
        resultingInfections.put(cityId, statesView.specializesViewFor(cityId).numberOfCubes(disease));
    }

}
