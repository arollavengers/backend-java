package arollavengers.core.domain.pandemic;

import arollavengers.core.infrastructure.Id;
import com.google.common.collect.Sets;
import org.springframework.util.Assert;

import java.util.Set;

public class Member {
    private final Id userId;
    private final MemberRole role;
    private final Set<PlayerCard> hand;

    public Member(Id userId, MemberRole role) {
        this.userId = userId;
        this.role = role;
        this.hand = Sets.newHashSet();
    }

    public Id userId() {
        return userId;
    }

    public MemberRole role() {
        return role;
    }

    public int handSize() {
        return hand.size();
    }

    public void addToHand(final PlayerCard card) {
        Assert.isTrue(handSize() < 7, "hand size should be less than 7");
        hand.add(card);
    }
}
