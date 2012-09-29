package arollavengers.core.domain.pandemic;

import com.google.common.collect.Multimap;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PathEnumerator {

    public static Listener shortestDriveOnly(final Multimap<CityId, Path[]> multimap) {
        return new Listener() {
            @Override
            public void routeFound(List<Path> route) {
                CityId dst = route.get(route.size() - 1).city;
                if (multimap.containsKey(dst)) {
                    if (containsDriveOnly(route)) {
                        Iterator<Path[]> iterator = multimap.get(dst).iterator();
                        while (iterator.hasNext()) {
                            Path[] presents = iterator.next();
                            if (route.size() > presents.length) {
                                // new route is longer than existing ones
                                // nothing more to do
                                return;
                            }
                            else if (route.size() == presents.length) {
                                multimap.put(dst, route.toArray(new Path[route.size()]));
                                return;
                            }
                            else {
                                // new route is shorter
                                iterator.remove();
                            }
                        }
                    }
                    else {
                        // make sure there is no drive only route that is shorter
                        for (Path[] presents : multimap.get(dst)) {
                            if (containsDriveOnly(presents)) {
                                if (presents.length <= route.size()) {
                                    return;
                                }
                            }
                        }
                        multimap.put(dst, route.toArray(new Path[route.size()]));
                    }
                }
                else {
                    multimap.put(dst, route.toArray(new Path[route.size()]));
                }
            }

            private boolean containsDriveOnly(List<Path> route) {
                for (Path path : route) {
                    if (path.move != null && path.move != MoveType.Drive) {
                        return false;
                    }
                }
                return true;
            }

            private boolean containsDriveOnly(Path[] route) {
                for (Path path : route) {
                    if (path.move != null && path.move != MoveType.Drive) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    private final BitSet visitedCities = new BitSet(CityId.nbCities());
    private final BitSet availableCityCards = new BitSet(CityId.nbCities());
    private final Stack<Path> paths = new Stack<Path>();
    private final Listener listener;
    private final CityGraph graph;
    private final int maxDepth;

    public PathEnumerator(Listener listener, CityGraph graph, int maxDepth) {
        this.listener = listener;
        this.graph = graph;
        this.maxDepth = maxDepth;
    }

    public void traverse(CityId cityId) {
        if (paths.size() >= maxDepth) {
            return;
        }

        if (hasBeenVisited(cityId)) {
            return;
        }
        markVisited(cityId);

        if (paths.empty()) {
            paths.add(new Path(cityId, null));
        }

        // ~~ check for card: jump aka CharterFlight
        if (availableCityCards.get(cityId.ordinal())) {
            // can go anywhere
            for (CityId other : CityId.values()) {
                if (hasBeenVisited(other)) {
                    continue;
                }
                paths.push(new Path(other, MoveType.CharterFlight));
                listener.routeFound(paths);
                paths.pop();
            }
        }

        for (CityId other : graph.adjacentCitiesOf(cityId)) {
            if (hasBeenVisited(other)) {
                continue;
            }
            paths.push(new Path(other, MoveType.Drive));
            listener.routeFound(paths);
            traverse(other);
            unmarkVisited(other);
            paths.pop();
        }
    }

    public boolean hasBeenVisited(CityId cityId) {
        return visitedCities.get(cityId.ordinal());
    }

    public void markVisited(CityId cityId) {
        visitedCities.set(cityId.ordinal());
    }

    public void unmarkVisited(CityId cityId) {
        visitedCities.clear(cityId.ordinal());
    }

    public void markAvailableCityCards(CityId... cities) {
        for (CityId cityId : cities) {
            availableCityCards.set(cityId.ordinal());
        }
    }

    public interface Listener {
        void routeFound(List<Path> route);
    }

    public static class Path {
        public final CityId city;
        public final MoveType move;

        public Path(CityId city, MoveType move) {
            this.city = city;
            this.move = move;
        }

        @Override
        public String toString() {
            return ((move != null) ? "-" + move + "->" : "") + "(" + city + ")";
        }
    }
}
