package arollavengers.core.domain.pandemic;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.Map;

public class MemberStates {
    private Map<Member, MemberState> memberStates;

    MemberStates() {
        memberStates = Maps.newHashMap();
    }

    public void createMemberState(Member member) {
        Preconditions.checkState(!memberStates.containsKey(member));
        memberStates.put(member, new MemberState());
    }

    public MemberState getStateOf(final Member member) {
        Preconditions.checkNotNull(member);
        return memberStates.get(member);
    }
}
