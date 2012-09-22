package arollavengers.core.domain.pandemic;

import static arollavengers.core.domain.pandemic.CityId.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CityGraph {

    private List<Link> links = new ArrayList<Link>();
    private List<Link> unmodifiableRoutesView = Collections.unmodifiableList(links);

    public CityGraph() {
        initializeBlackLinks();
        initializeBlueLinks();
        initializeOrangeLinks();
        initializeYellowLinks();
    }

    /**
     * @return an unmodifiable view of all the links currently defined.
     */
    public List<Link> getLinks() {
        return unmodifiableRoutesView;
    }

    /**
     * Returns the list of the cities that are linked to the specified one.
     *
     * @param cityId the city from which one wants to retrieve all the linked ones.
     * @return the list of the cities that are linked
     */
    public EnumSet<CityId> listCitiesLinkedTo(CityId cityId) {
        EnumSet<CityId> cityIds = EnumSet.noneOf(CityId.class);
        for (Link link : links) {
            if (link.contains(cityId)) {
                cityIds.add(link.other(cityId));
            }
        }
        return cityIds;
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
        Link r = new Link(cityIdOne, cityIdTwo);
        for (Link link : links) {
            if (link.sameAs(r)) {
                return;
            }
        }
        links.add(r);
    }

    /**
     * Describe a connection between two cities.
     */
    public static class Link {
        private final CityId cityIdOne;
        private final CityId cityIdTwo;

        public Link(CityId cityIdOne, CityId cityIdTwo) {
            this.cityIdOne = cityIdOne;
            this.cityIdTwo = cityIdTwo;
        }

        /**
         * Indicates whether or not this route is connected to the specified cityId.
         *
         * @param cityId the specified cityId
         * @return <code>true</code> if this route is connected to the specified cityId.
         */
        public boolean contains(CityId cityId) {
            return cityIdOne == cityId || cityIdTwo == cityId;
        }

        public boolean sameAs(Link link) {
            return (link.cityIdOne == cityIdOne && link.cityIdTwo == cityIdTwo)
                    || (link.cityIdTwo == cityIdOne && link.cityIdOne == cityIdTwo);
        }

        @Override
        public String toString() {
            return "Link{" + cityIdOne + " <--> " + cityIdTwo + '}';
        }

        public CityId other(CityId cityId) {
            if (cityId == cityIdOne) {
                return cityIdTwo;
            }
            else {
                return cityIdOne;
            }
        }
    }
}
