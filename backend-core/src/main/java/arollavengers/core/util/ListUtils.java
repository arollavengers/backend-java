package arollavengers.core.util;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ListUtils {

    public static <E, R> R foldLeft(Iterable<E> elements, R initial, Function2<R, E, R> foldFunction) {
        R value = initial;
        for (E element : elements) {
            value = foldFunction.apply(value, element);
        }
        return value;
    }

    public static <E> List<List<E>> split(List<E> elements, int nbParts) {
        int pileSize = elements.size() / nbParts;
        int remaining = elements.size() % nbParts;

        List<List<E>> listOfList = Lists.newArrayList();

        int fromIndex = 0;
        for (int i = 0; i < nbParts; i++) {
            int toIndex = fromIndex + pileSize;
            if (remaining-- > 0) {
                toIndex++;
            }
            if (fromIndex < elements.size() && toIndex <= elements.size()) {
                // Note: sublist return a view of the original list, not a copy
                // since lists can be modified, one must create our own copy
                listOfList.add(Lists.newArrayList(elements.subList(fromIndex, toIndex)));
            }
            else {
                listOfList.add(Lists.<E>newArrayList());
            }

            fromIndex = toIndex;
        }
        return listOfList;
    }
}
