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

import com.google.common.collect.Maps;

import java.util.BitSet;
import java.util.EnumSet;
import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CityGraph {

    public static CityGraph getInstance() {
        return new CityGraph().initializeDefaultLinks();
    }

    private final Map<CityId, BitSet> adjacencyMatrix = Maps.newHashMap();

    /**
     * Create a new and empty graph.
     *
     * @see #initializeDefaultLinks()
     */
    public CityGraph() {
    }

    public CityGraph initializeDefaultLinks() {
        initializeBlackLinks();
        initializeBlueLinks();
        initializeOrangeLinks();
        initializeYellowLinks();
        return this;
    }

    /**
     * Returns the list of the cities that are linked to the specified one.
     *
     * @param cityId the city from which one wants to retrieve all the linked ones.
     * @return the list of the cities that are linked
     */
    public EnumSet<CityId> adjacentCitiesOf(CityId cityId) {
        CityId[] cities = CityId.values();

        EnumSet<CityId> cityIds = EnumSet.noneOf(CityId.class);
        BitSet bs = getOrCreateBitSet(cityId);
        for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
             // operate on index i here
            CityId linked = cities[i];
            if(linked!=cityId)
                cityIds.add(linked);
        }
        return cityIds;
    }

    public boolean areAdjacent(CityId one, CityId two) {
        BitSet bitSet = getOrCreateBitSet(one);
        return bitSet.get(two.ordinal());
    }

    protected void initializeBlueLinks() {
        // ~~~ Blue
        define(SanFrancisco, Tokyo);
        define(SanFrancisco, Manila);
        define(SanFrancisco, LosAngeles);
        define(SanFrancisco, Chicago);
        define(Chicago, SanFrancisco);
        define(Chicago, LosAngeles);
        define(Chicago, MexicoCity);
        define(Chicago, Atlanta);
        define(Chicago, Toronto);
        define(Atlanta, Chicago);
        define(Atlanta, Miami);
        define(Atlanta, Washington);
        define(Toronto, Chicago);
        define(Toronto, Washington);
        define(Toronto, NewYork);
        define(Washington, NewYork);
        define(Washington, Toronto);
        define(Washington, Atlanta);
        define(Washington, Miami);
        define(NewYork, Toronto);
        define(NewYork, Washington);
        define(NewYork, Madrid);
        define(NewYork, London);
        define(Madrid, NewYork);
        define(Madrid, SaoPaulo);
        define(Madrid, Algiers);
        define(Madrid, Paris);
        define(Madrid, London);
        define(London, NewYork);
        define(London, Madrid);
        define(London, Paris);
        define(London, Essen);
        define(Paris, London);
        define(Paris, Madrid);
        define(Paris, Algiers);
        define(Paris, Milan);
        define(Paris, Essen);
        define(Essen, London);
        define(Essen, Paris);
        define(Essen, Milan);
        define(Essen, SaintPetersburg);
        define(Milan, Essen);
        define(Milan, Paris);
        define(Milan, Istanbul);
        define(SaintPetersburg, Essen);
        define(SaintPetersburg, Istanbul);
        define(SaintPetersburg, Moscow);
    }

    protected void initializeBlackLinks() {
        // ~~~ Black
        define(Algiers, Madrid);
        define(Algiers, Paris);
        define(Algiers, Istanbul);
        define(Algiers, Cairo);
        define(Istanbul, Algiers);
        define(Istanbul, Milan);
        define(Istanbul, SaintPetersburg);
        define(Istanbul, Moscow);
        define(Istanbul, Baghdad);
        define(Istanbul, Cairo);
        define(Moscow, SaintPetersburg);
        define(Moscow, Tehran);
        define(Moscow, Istanbul);
        define(Tehran, Moscow);
        define(Tehran, Baghdad);
        define(Tehran, Karachi);
        define(Tehran, Delhi);
        define(Delhi, Tehran);
        define(Delhi, Karachi);
        define(Delhi, Mumbai);
        define(Delhi, Chennai);
        define(Delhi, Kolkata);
        define(Kolkata, Delhi);
        define(Kolkata, HongKong);
        define(Kolkata, Bangkok);
        define(Kolkata, Chennai);
        define(Chennai, Kolkata);
        define(Chennai, Bangkok);
        define(Chennai, Jakarta);
        define(Chennai, Mumbai);
        define(Chennai, Delhi);
        define(Mumbai, Karachi);
        define(Mumbai, Delhi);
        define(Mumbai, Chennai);
        define(Karachi, Baghdad);
        define(Karachi, Tehran);
        define(Karachi, Delhi);
        define(Karachi, Mumbai);
        define(Karachi, Riyadh);
        define(Baghdad, Istanbul);
        define(Baghdad, Tehran);
        define(Baghdad, Karachi);
        define(Baghdad, Riyadh);
        define(Baghdad, Cairo);
        define(Cairo, Algiers);
        define(Cairo, Istanbul);
        define(Cairo, Baghdad);
        define(Cairo, Riyadh);
        define(Cairo, Khartoum);
        define(Riyadh, Cairo);
        define(Riyadh, Baghdad);
        define(Riyadh, Karachi);
    }

    protected void initializeYellowLinks() {
        // ~~~ Yellow
    }

    protected void initializeOrangeLinks() {
        // ~~~ Orange
    }


    private void define(CityId cityIdOne, CityId cityIdTwo) {
        BitSet bitSetOne = getOrCreateBitSet(cityIdOne);
        bitSetOne.set(cityIdTwo.ordinal());
        BitSet bitSetTwo = getOrCreateBitSet(cityIdTwo);
        bitSetTwo.set(cityIdOne.ordinal());
    }

    private BitSet getOrCreateBitSet(CityId cityId) {
        BitSet bitSet = adjacencyMatrix.get(cityId);
        if (bitSet == null) {
            bitSet = new BitSet(CityId.nbCities());
            adjacencyMatrix.put(cityId, bitSet);
        }
        return bitSet;
    }

}
