package arollavengers.core.domain.pandemic;

import arollavengers.core.exceptions.pandemic.PandemicRuntimeException;
import arollavengers.core.infrastructure.Id;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Team implements Iterable<Member> {

    private final Map<MemberKey, Member> team = Maps.newHashMap();

    public Set<MemberRole> roles() {
        Set<MemberRole> roles = new HashSet<MemberRole>();
        for (Member member : team.values()) {
            roles.add(member.role());
        }
        return roles;
    }

    public Optional<Member> findMember(@Nonnull Id userId) {
        for (Member member : team.values()) {
            if (member.memberKey().userId().equals(userId)) {
                return Optional.of(member);
            }
        }
        return Optional.absent();
    }

    public Optional<Member> findMember(@Nonnull MemberKey memberKey) {
        Member member = team.get(memberKey);
        if (member != null) {
            return Optional.of(member);
        }
        else {
            return Optional.absent();
        }
    }

    public boolean hasRole(MemberRole role) {
        return roles().contains(role);
    }

    void enrole(final Member member) {
        team.put(member.memberKey(), member);
    }

    public int size() {
        return team.size();
    }

    @Override
    public Iterator<Member> iterator() {
        return team.values().iterator();
    }

    public Member getMemberAtPosition(int position) {
        for (Member member : team.values()) {
            if (member.positionOnTable() == position) {
                return member;
            }
        }
        throw new PandemicRuntimeException("No member defined at position " + position);
    }
}
