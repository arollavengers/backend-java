package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.domain.pandemic.Disease;
import arollavengers.core.domain.pandemic.OutbreakGenerationChain;
import arollavengers.core.infrastructure.Id;
import com.google.common.collect.Multimap;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import java.util.EnumMap;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OutbreakChainTriggeredEvent implements CityEvent {

    @JsonProperty
    private final Id worldId;

    @JsonProperty
    private final OutbreakGenerationChain.OutbreakGenerationMap generationMap;

    @JsonProperty
    private final Disease disease;

    @JsonProperty
    private final EnumMap<CityId, Integer> resultingInfections;

    @JsonCreator
    public OutbreakChainTriggeredEvent(@JsonProperty("worldId") Id worldId,
                                       @JsonProperty("generations") OutbreakGenerationChain.OutbreakGenerationMap generationMap,
                                       @JsonProperty("diseasz") Disease disease,
                                       @JsonProperty("resultinInfections") EnumMap<CityId, Integer> resultingInfections)
    {
        this.worldId = worldId;
        this.generationMap = generationMap;
        this.disease = disease;
        this.resultingInfections = resultingInfections;
    }

    @Override
    public Id entityId() {
        return worldId;
    }

    public Disease disease() {
        return disease;
    }

    public OutbreakGenerationChain.OutbreakGenerationMap generations() {
        return generationMap;
    }

    public EnumMap<CityId, Integer> resultingInfections() {
        return resultingInfections;
    }

    @Override
    public String toString() {
        return "OutbreakChainTriggeredEvent[" + worldId
                + ", " + generationMap
                + ", " + resultingInfections + "]";
    }
}
