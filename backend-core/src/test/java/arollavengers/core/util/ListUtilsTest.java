package arollavengers.core.util;

import static org.fest.assertions.api.Assertions.assertThat;

import com.google.common.collect.Lists;

import org.junit.Test;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ListUtilsTest {

    @Test
    public void split_empty_list() throws Exception {
        List<List<String>> lists = ListUtils.split(sample(0), 4);
        assertThat(lists).isNotNull();
        assertThat(lists).hasSize(4);
        assertThat(lists.get(0)).isEmpty();
        assertThat(lists.get(1)).isEmpty();
        assertThat(lists.get(2)).isEmpty();
        assertThat(lists.get(3)).isEmpty();
    }

    @Test
    public void split_less_elements_than_nb_parts_case_1() throws Exception {
        List<List<String>> lists = ListUtils.split(sample(2), 4);
        assertThat(lists).isNotNull();
        assertThat(lists).hasSize(4);
        assertThat(lists.get(0)).hasSize(1);
        assertThat(lists.get(1)).hasSize(1);
        assertThat(lists.get(2)).isEmpty();
        assertThat(lists.get(3)).isEmpty();
    }

    @Test
    public void split_same_number_of_elements_than_nb_parts() throws Exception {
        List<List<String>> lists = ListUtils.split(sample(3), 3);
        assertThat(lists).isNotNull();
        assertThat(lists).hasSize(3);
        assertThat(lists.get(0)).hasSize(1);
        assertThat(lists.get(1)).hasSize(1);
        assertThat(lists.get(1)).hasSize(1);
    }

    @Test
    public void split_more_elements_than_nb_parts__multiple_case() throws Exception {
        List<List<String>> lists = ListUtils.split(sample(9), 3);
        assertThat(lists).isNotNull();
        assertThat(lists).hasSize(3);
        assertThat(lists.get(0)).hasSize(3);
        assertThat(lists.get(1)).hasSize(3);
        assertThat(lists.get(1)).hasSize(3);
    }

    @Test
    public void split_more_elements_than_nb_parts__non_multiple_case() throws Exception {
        List<List<String>> lists = ListUtils.split(sample(7), 3);
        assertThat(lists).isNotNull();
        assertThat(lists).hasSize(3);
        assertThat(lists.get(0)).hasSize(3);
        assertThat(lists.get(1)).hasSize(2);
        assertThat(lists.get(1)).hasSize(2);
    }

    private static List<String> sample(int nbElements) {
        List<String> elements = Lists.newArrayList();
        for(int i=0;i<nbElements;i++)
            elements.add(String.valueOf(i));
        return elements;
    }
}
