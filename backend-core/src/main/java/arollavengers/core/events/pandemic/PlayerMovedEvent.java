package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.domain.pandemic.MoveType;
import arollavengers.core.domain.pandemic.PlayerCard;
import arollavengers.core.infrastructure.Id;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PlayerMovedEvent implements MemberEvent {

    @JsonProperty
    private final Id memberId;

    @JsonProperty("city")
    private final CityId cityId;

    @JsonProperty
    private final MoveType moveType;

    @JsonProperty
    private final PlayerCard cardUsed;

    @JsonCreator
    public PlayerMovedEvent(@Nonnull @JsonProperty("memberId") Id memberId,
                            @Nonnull @JsonProperty("city") CityId cityId,
                            @Nonnull @JsonProperty("moveType") MoveType moveType,
                            @Nullable @JsonProperty("cardUsed") PlayerCard cardUsed)
    {
        this.memberId = memberId;
        this.cityId = cityId;
        this.moveType = moveType;
        this.cardUsed = cardUsed;
    }

    @Override
    public Id entityId() {
        return memberId;
    }

    public CityId cityId () {
        return cityId;
    }

    public PlayerCard cardUsed() {
        return cardUsed;
    }

    @Override
    public String toString() {
        return "PlayerMovedEvent[" + entityId() +
                ", " + cityId + "/" + moveType +
                ", card: " + cardUsed +
                "]";
    }
}
