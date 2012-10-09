package arollavengers.core.domain.pandemic;

import arollavengers.core.infrastructure.Id;
import arollavengers.pattern.annotation.ValueObject;

import org.codehaus.jackson.annotate.JsonProperty;
import javax.annotation.Nonnull;
import javax.validation.constraints.Pattern;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@ValueObject
public class MemberKey {
    @JsonProperty
    private final Id userId;

    @JsonProperty
    private final MemberRole role;

    public MemberKey(@Nonnull @JsonProperty("userId") Id userId,
                     @Nonnull @JsonProperty("role") MemberRole role) {
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

    public String asString () {
        return userId().asString() + "/" + role.name();
    }

    public static MemberKey fromString(@Nonnull String memberKeyAsString) {
        String[] split = memberKeyAsString.split("/");
        if(split.length != 2) {
            throw new IllegalArgumentException("Invalid MemberKey string representation of <" + memberKeyAsString + ">");
        }
        Id id = Id.create(split[0]);
        MemberRole role = MemberRole.valueOf(split[1]);
        return new MemberKey(id, role);
    }
}
