package arollavengers.core.domain.pandemic;

import arollavengers.core.infrastructure.Id;
import com.google.common.base.Optional;

import java.util.HashSet;
import java.util.Set;

public class Team {

    private final Set<Member> team = new HashSet<Member>();

    public Set<MemberRole> roles() {
        Set<MemberRole> roles = new HashSet<MemberRole>();
        for (Member member : team) {
            roles.add(member.role());
        }
        return roles;
    }

    public Optional<Member> findMember(Id userId) {
        for (Member member : team) {
            if (member.userId().equals(userId)) {
                return Optional.of(member);
            }
        }
        return Optional.absent();
    }

    public boolean hasRole(MemberRole role) {
        return roles().contains(role);
    }

    void enrole(final Member member) {
        team.add(member);
    }

}
