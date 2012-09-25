package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.domain.pandemic.MoveType;
import arollavengers.core.infrastructure.Id;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PlayerMovedEvent implements MemberEvent {

    private final Id memberId;
    private final CityId city;
    private final MoveType moveType;
    private long version;

    public PlayerMovedEvent(Id memberId, CityId city, MoveType moveType) {
        this.memberId = memberId;
        this.city = city;
        this.moveType = moveType;
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

    @Override
    public String toString() {
        return "PlayerMovedEvent[" + entityId() +
                ", v" + version +
                ", " + city + "/" + moveType +
                "]";
    }
}
