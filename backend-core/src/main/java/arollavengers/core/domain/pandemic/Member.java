package arollavengers.core.domain.pandemic;

import arollavengers.core.events.pandemic.PlayerCardDrawnFromPileEvent;
import arollavengers.core.events.pandemic.WorldEvent;
import arollavengers.core.infrastructure.Aggregate;
import arollavengers.core.infrastructure.AnnotationBasedEventHandler;
import arollavengers.core.infrastructure.Entity;
import arollavengers.core.infrastructure.EventHandler;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.annotation.OnEvent;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.util.List;

public class Member extends Entity<WorldEvent> {

    private final EventHandler<WorldEvent> eventHandler;
    //
    private final List<PlayerCard> hand = Lists.newArrayList();
    private final MemberKey memberKey;

    public Member(@Nonnull Aggregate<WorldEvent> aggregate, @Nonnull Id entityId, Id userId, MemberRole role) {
        super(aggregate, entityId);
        this.eventHandler = new AnnotationBasedEventHandler<WorldEvent>(this);
        this.memberKey = new MemberKey(userId, role);
    }

    @Override
    protected EventHandler<WorldEvent> internalEventHandler() {
        return eventHandler;
    }

    public MemberKey memberKey() {
        return memberKey;
    }

    public MemberRole role() {
        return memberKey.role();
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

    @OnEvent
    private void onCardDrawn(final PlayerCardDrawnFromPileEvent event) {
        hand.add(event.playerCard());
    }

    @Override
    public String toString() {
        return "Member[" + memberKey.userId() + ", " + memberKey.role() + ']';
    }

}
