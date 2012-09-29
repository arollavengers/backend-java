package arollavengers.core.domain.pandemic;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PathEnumeratorTest {

    private PathEnumerator pathEnumerator;

    @Before
    public void setUp() {
        pathEnumerator = new PathEnumerator(new PathEnumerator.Listener() {
            @Override
            public void routeFound(List<PathEnumerator.Path> path) {
                System.out.println(path);
            }
        },
                CityGraph.getInstance(), 4);
    }

    @Test
    public void sample1() {
        pathEnumerator.markAvailableCityCards(CityId.Essen, CityId.SaintPetersburg);
        pathEnumerator.traverse(CityId.Paris);
    }

    @Test
    public void sample2() {
        Multimap<CityId, PathEnumerator.Path[]> multimap = ArrayListMultimap.create();
        pathEnumerator = new PathEnumerator(PathEnumerator.shortestDriveOnly(multimap),
                CityGraph.getInstance(), 4);
        pathEnumerator.markAvailableCityCards(CityId.Essen, CityId.SaintPetersburg);
        pathEnumerator.traverse(CityId.Paris);

        for (CityId cityId : multimap.keySet()) {
            for (PathEnumerator.Path[] route : multimap.get(cityId)) {
                System.out.println(Arrays.toString(route));
            }
        }
    }
}
