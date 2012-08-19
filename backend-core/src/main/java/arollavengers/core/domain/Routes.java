package arollavengers.core.domain;

import static arollavengers.core.domain.City.Algiers;
import static arollavengers.core.domain.City.Atlanta;
import static arollavengers.core.domain.City.Chicago;
import static arollavengers.core.domain.City.Essen;
import static arollavengers.core.domain.City.Istanbul;
import static arollavengers.core.domain.City.London;
import static arollavengers.core.domain.City.LosAngeles;
import static arollavengers.core.domain.City.Madrid;
import static arollavengers.core.domain.City.Manila;
import static arollavengers.core.domain.City.Miami;
import static arollavengers.core.domain.City.MexicoCity;
import static arollavengers.core.domain.City.Milan;
import static arollavengers.core.domain.City.Moscow;
import static arollavengers.core.domain.City.NewYork;
import static arollavengers.core.domain.City.Paris;
import static arollavengers.core.domain.City.SaintPetersburg;
import static arollavengers.core.domain.City.SanFrancisco;
import static arollavengers.core.domain.City.SaoPaulo;
import static arollavengers.core.domain.City.Tokyo;
import static arollavengers.core.domain.City.Toronto;
import static arollavengers.core.domain.City.Washington;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Routes {

    private List<Route> routes = new ArrayList<Route>();
    private List<Route> unmodifiableRoutesView = Collections.unmodifiableList(routes);

    /**
     * Returns an unmodifiable view of all the routes currently defined.
     */
    public List<Route> getRoutes() {
        return unmodifiableRoutesView;
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


    private void define(City cityOne, City cityTwo) {
        Route r = new Route(cityOne, cityTwo);
        for(Route route : routes) {
            if(route.sameAs(r))
                return;
        }
        routes.add(r);
    }

    /**
     * Describe a connection between two cities.
     */
    public static class Route {
        private final City cityOne;
        private final City cityTwo;
        public Route(City cityOne, City cityTwo) {
            this.cityOne = cityOne;
            this.cityTwo = cityTwo;
        }

        /**
         * Indicates whether or not this route is connected to the specified city.
         * @param city
         * @return <code>true</code> if this route is connected to the specified city.
         */
        public boolean connects(City city) {
            return cityOne==city || cityTwo==city;
        }

        public boolean sameAs(Route route) {
            return (route.cityOne==cityOne && route.cityTwo==cityTwo)
                    || (route.cityTwo==cityOne && route.cityOne==cityTwo);
        }

        @Override
        public String toString() {
            return "Route{" + cityOne + " <--> " + cityTwo + '}';
        }
    }
}
