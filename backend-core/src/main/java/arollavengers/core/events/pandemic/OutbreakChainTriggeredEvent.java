package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.domain.pandemic.Disease;
import arollavengers.core.domain.pandemic.OutbreakChain;
import arollavengers.core.domain.pandemic.OutbreakGenerationChain;
import arollavengers.core.infrastructure.Id;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.EnumMap;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OutbreakChainTriggeredEvent implements CityEvent {

    private long version;
    private final Id worldId;
    private final Multimap<Integer, CityId> generations;
    private final Disease disease;
    private final EnumMap<CityId, Integer> resultingInfections;

    public OutbreakChainTriggeredEvent(Id worldId,
                                       Multimap<Integer, CityId> generations, Disease disease,
                                       EnumMap<CityId, Integer> resultingInfections)
    {
        this.worldId = worldId;
        this.generations = generations;
        this.disease = disease;
        this.resultingInfections = resultingInfections;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public Id entityId() {
        return worldId;
    }

    @Override
    public void assignVersion(long version) {
        this.version = version;
    }

    public Disease disease() {
        return disease;
    }

    public Multimap<Integer, CityId> generations() {
        return generations;
    }

    public EnumMap<CityId, Integer> resultingInfections() {
        return resultingInfections;
    }

    @Override
    public String toString() {
        return "OutbreakChainTriggeredEvent[" + worldId + ", v" + version + ", " + generations + ", " + resultingInfections + "]";
    }
}
