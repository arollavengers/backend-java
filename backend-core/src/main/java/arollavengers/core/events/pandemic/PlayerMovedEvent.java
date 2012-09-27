package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.domain.pandemic.MoveType;
import arollavengers.core.domain.pandemic.PlayerCard;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class")
public class PlayerMovedEvent implements MemberEvent {

    @JsonProperty
    private final Id memberId;

    @JsonProperty
    private final CityId city;

    @JsonProperty
    private final MoveType moveType;

    @JsonProperty
    private final PlayerCard cardUsed;

    @JsonProperty
    private long version;

    @JsonCreator
    public PlayerMovedEvent(@Nonnull @JsonProperty("memberId") Id memberId,
                            @Nonnull @JsonProperty("city") CityId city,
                            @Nonnull @JsonProperty("moveType") MoveType moveType,
                            @Nullable @JsonProperty("cardUsed") PlayerCard cardUsed)
    {
        this.memberId = memberId;
        this.city = city;
        this.moveType = moveType;
        this.cardUsed = cardUsed;
    }

    @Override
    public Id entityId() {
        return memberId;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public void assignVersion(long version) {
        this.version = version;
    }

    public CityId cityId() {
        return city;
    }

    public PlayerCard cardUsed() {
        return cardUsed;
    }

    @Override
    public String toString() {
        return "PlayerMovedEvent[" + entityId() +
                ", v" + version +
                ", " + city + "/" + moveType +
                ", card: " + cardUsed +
                "]";
    }
}
