package arollavengers.core.domain.pandemic;

import arollavengers.core.infrastructure.Id;
import com.google.common.base.Optional;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Team implements Iterable<Member> {

    private final Set<Member> team = new HashSet<Member>();

    public Set<MemberRole> roles() {
        Set<MemberRole> roles = new HashSet<MemberRole>();
        for (Member member : team) {
            roles.add(member.role());
        }
        return roles;
    }

    public Optional<Member> findMember(@NotNull Id userId) {
        for (Member member : team) {
            if (member.userId().equals(userId)) {
                return Optional.of(member);
            }
        }
        return Optional.absent();
    }

    public Optional<Member> findMember(@NotNull MemberRole memberRole) {
        for (Member member : team) {
            if (member.role().equals(memberRole)) {
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

    public int size() {
        return team.size();
    }

    @Override
    public Iterator<Member> iterator() {
        return team.iterator();
    }
}
