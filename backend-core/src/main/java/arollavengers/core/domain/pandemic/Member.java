package arollavengers.core.domain.pandemic;

import arollavengers.core.infrastructure.Id;
import arollavengers.pattern.annotation.ValueObject;

@ValueObject
public class Member {
    private final Id userId;
    private final MemberRole role;

    public Member(Id userId, MemberRole role) {
        this.userId = userId;
        this.role = role;
    }

    public Id userId() {
        return userId;
    }

    public MemberRole role() {
        return role;
    }

    @Override
    public String toString() {
        return "Member[" + userId + ", " + role + ']';
    }
}
