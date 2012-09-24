package arollavengers.core.domain.pandemic;

import arollavengers.core.infrastructure.Id;
import arollavengers.pattern.annotation.ValueObject;

import javax.annotation.Nonnull;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@ValueObject
public class MemberKey {
    private final Id userId;
    private final MemberRole role;

    public MemberKey(@Nonnull Id userId, @Nonnull MemberRole role) {
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MemberKey memberKey = (MemberKey) o;

        if (role != memberKey.role) {
            return false;
        }
        if (!userId.equals(memberKey.userId)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + role.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MemberKey{" +
                "userId=" + userId +
                ", role=" + role +
                '}';
    }
}
