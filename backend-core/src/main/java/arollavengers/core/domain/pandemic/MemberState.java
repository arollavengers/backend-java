package arollavengers.core.domain.pandemic;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import java.util.Set;

public class MemberState {

    private Set<PlayerCard> hand;

    public MemberState() {
        this.hand = Sets.newHashSet();
    }

    public void ensureActionIsAuthorized() {
        throw new RuntimeException("not implemented");
    }

    public int handSize() {
        return hand.size();
    }

    public void addToHand(final PlayerCard card) {
        Preconditions.checkState(handSize() < 7, "hand size should be less than 7");
        hand.add(card);
    }
}
