package arollavengers.core.usecase;

import arollavengers.core.testutils.TypeOfEventStore;
import arollavengers.core.util.Objects;
import arollavengers.junit.LabeledParameterized;
import com.google.common.collect.Lists;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@SuppressWarnings("ConstantConditions")
@RunWith(LabeledParameterized.class)
public class WorldServiceUsecaseAllEventStoreTest extends WorldServiceUsecaseTest {

    /**
     * Test will be executed on all different event store implementations.
     *
     * @see arollavengers.core.testutils.TypeOfEventStore
     */
    @LabeledParameterized.Parameters
    public static List<Object[]> parameters() {
        List<Object[]> modes = Lists.newArrayList();
        for (TypeOfEventStore typeOfEventStore : TypeOfEventStore.values()) {
            modes.add(Objects.o(typeOfEventStore));
        }
        return modes;
    }

    public WorldServiceUsecaseAllEventStoreTest(TypeOfEventStore typeOfEventStore) {
        super(typeOfEventStore);
    }
}
