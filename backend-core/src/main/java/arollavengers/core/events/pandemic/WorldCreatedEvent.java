package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.Conf;
import arollavengers.core.domain.pandemic.Difficulty;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class WorldCreatedEvent implements PandemicEvent {

    @JsonProperty
    private long version;

    @JsonProperty
    private final Id newWorldId;

    @JsonProperty
    private final Id ownerId;

    @JsonProperty
    private final Difficulty difficulty;

    @JsonProperty
    private final Conf conf;

    @JsonCreator
    public WorldCreatedEvent(@JsonProperty("newWorldId") Id newWorldId,
                             @JsonProperty("ownerId") Id ownerId,
                             @JsonProperty("difficulty") Difficulty difficulty,
                             @JsonProperty("conf") Conf conf)
    {
        this.newWorldId = newWorldId;
        this.ownerId = ownerId;
        this.difficulty = difficulty;
        this.conf = conf;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public void assignVersion(final long version) {
        this.version = version;
    }

    @Override
    public Id entityId() {
        return newWorldId;
    }

    public Difficulty difficulty() {
        return difficulty;
    }

    public Id ownerId() {
        return ownerId;
    }

    public Conf conf() {
        return conf;
    }

    @Override
    public String toString() {
        return "WorldCreatedEvent[" + newWorldId
                + ", v" + version
                + ", owner: " + ownerId
                + ", difficulty: " + difficulty
                + ", conf: " + conf
                + "]";
    }


}
