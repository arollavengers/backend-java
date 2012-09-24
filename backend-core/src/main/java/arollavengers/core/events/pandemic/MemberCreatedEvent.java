package arollavengers.core.events.pandemic;

import arollavengers.core.domain.pandemic.MemberRole;
import arollavengers.core.domain.pandemic.PlayerCard;
import arollavengers.core.infrastructure.Id;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class MemberCreatedEvent implements MemberEvent {

    private long version;
    private final Id memberId;
    private final Id userId;
    private final MemberRole role;

    public MemberCreatedEvent(Id memberId, Id userId, MemberRole role) {
        this.memberId = memberId;
        this.userId = userId;
        this.role = role;
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

    @Override
    public String toString() {
        return "MemberCreatedEvent[" + memberId +
                ", v" + version +
                ", user=" + userId +
                ", role=" + role +
                "]";
    }
}
