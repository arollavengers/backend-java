package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.domain.pandemic.Disease;
import arollavengers.core.domain.pandemic.OutbreakChain;
import arollavengers.core.domain.pandemic.OutbreakGenerationChain;
import arollavengers.core.infrastructure.Id;
import com.google.common.collect.Multimap;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import java.util.Collection;
import java.util.EnumMap;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class")
public class OutbreakChainTriggeredEvent implements CityEvent {

    @JsonProperty
    private long version;

    @JsonProperty
    private final Id worldId;

    @JsonProperty
    private final Multimap<Integer, CityId> generations;

    @JsonProperty
    private final Disease disease;

    @JsonProperty
    private final EnumMap<CityId, Integer> resultingInfections;

    @JsonCreator
    public OutbreakChainTriggeredEvent(@JsonProperty("worldId") Id worldId,
                                       @JsonProperty("generations") Multimap<Integer, CityId> generations,
                                       @JsonProperty("diseasz") Disease disease,
                                       @JsonProperty("resultinInfections") EnumMap<CityId, Integer> resultingInfections)
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
