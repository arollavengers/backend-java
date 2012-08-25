package arollavengers.core.domain;

import static arollavengers.core.domain.CityId.Algiers;
import static arollavengers.core.domain.CityId.Atlanta;
import static arollavengers.core.domain.CityId.Chicago;
import static arollavengers.core.domain.CityId.Essen;
import static arollavengers.core.domain.CityId.Istanbul;
import static arollavengers.core.domain.CityId.London;
import static arollavengers.core.domain.CityId.LosAngeles;
import static arollavengers.core.domain.CityId.Madrid;
import static arollavengers.core.domain.CityId.Manila;
import static arollavengers.core.domain.CityId.MexicoCity;
import static arollavengers.core.domain.CityId.Miami;
import static arollavengers.core.domain.CityId.Milan;
import static arollavengers.core.domain.CityId.Moscow;
import static arollavengers.core.domain.CityId.NewYork;
import static arollavengers.core.domain.CityId.Paris;
import static arollavengers.core.domain.CityId.SaintPetersburg;
import static arollavengers.core.domain.CityId.SanFrancisco;
import static arollavengers.core.domain.CityId.SaoPaulo;
import static arollavengers.core.domain.CityId.Tokyo;
import static arollavengers.core.domain.CityId.Toronto;
import static arollavengers.core.domain.CityId.Washington;

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

    /**
     * @return an unmodifiable view of all the links currently defined.
     */
    public List<Link> getLinks() {
        return unmodifiableRoutesView;
    }

    public EnumSet<CityId> listCitiesConnectedTo(CityId cityId) {
        EnumSet<CityId> cityIds = EnumSet.noneOf(CityId.class);
        for (Link link : links) {
            if (link.contains(cityId)) {
                cityIds.add(link.other(cityId));
            }
        }
        return cityIds;
    }

    protected void initialize() {
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
        // ~~~ Black
        // ~~~ Yellow
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
