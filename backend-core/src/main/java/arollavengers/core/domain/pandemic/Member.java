package arollavengers.core.domain.pandemic;

import arollavengers.core.events.pandemic.CurrentPlayerDefinedEvent;
import arollavengers.core.events.pandemic.PlayerCardDrawnFromPileEvent;
import arollavengers.core.events.pandemic.PlayerMovedEvent;
import arollavengers.core.events.pandemic.WorldEvent;
import arollavengers.core.exceptions.pandemic.ActionNotAuthorizedException;
import arollavengers.core.exceptions.pandemic.HandSizeLimitReachedException;
import arollavengers.core.infrastructure.Aggregate;
import arollavengers.core.infrastructure.AnnotationBasedEventHandler;
import arollavengers.core.infrastructure.Entity;
import arollavengers.core.infrastructure.EventHandler;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.annotation.OnEvent;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.util.List;

public class Member extends Entity<WorldEvent> {

    private final EventHandler<WorldEvent> eventHandler;
    //
    private final List<PlayerCard> hand = Lists.newArrayList();
    private final MemberKey memberKey;
    private boolean currentPlayer;
    private int nbActionRemaining;
    private CityId location;

    public Member(@Nonnull Aggregate<WorldEvent> aggregate, @Nonnull Id entityId, @Nonnull Id userId, @Nonnull MemberRole role) {
        super(aggregate, entityId);
        this.eventHandler = new AnnotationBasedEventHandler<WorldEvent>(this);
        this.memberKey = new MemberKey(userId, role);
    }

    @Override
    protected EventHandler<WorldEvent> internalEventHandler() {
        return eventHandler;
    }

    @Nonnull
    public MemberKey memberKey() {
        return memberKey;
    }

    @Nonnull
    public MemberRole role() {
        return memberKey.role();
    }

    public void ensureActionIsAuthorized() {
        if(!currentPlayer || nbActionRemaining<=0)
            throw new ActionNotAuthorizedException(currentPlayer, nbActionRemaining);
    }

    public int handSize() {
        return hand.size();
    }

    public void addToHand(@Nonnull PlayerCard card) {
        if(handSize() >= 7)
            throw new HandSizeLimitReachedException(entityId(), handSize());
        applyNewEvent(new PlayerCardDrawnFromPileEvent(entityId(), card));
    }

    @OnEvent
    private void onCardDrawn(final PlayerCardDrawnFromPileEvent event) {
        hand.add(event.playerCard());
    }

    @Override
    public String toString() {
        return "Member[" + memberKey.userId() + ", " + memberKey.role() + ']';
    }

    public boolean isCurrentPlayer() {
        return currentPlayer;
    }

    public void defineAsCurrentPlayer(int nbActions) {
        applyNewEvent(new CurrentPlayerDefinedEvent(entityId(), nbActions));
    }

    @OnEvent
    private void onCurrentPlayerDefined(CurrentPlayerDefinedEvent event) {
        this.currentPlayer = true;
        this.nbActionRemaining = event.nbActions();
    }

    public void moveTo(@Nonnull CityId cityId, @Nonnull MoveType moveType) {
        applyNewEvent(new PlayerMovedEvent(entityId(), cityId, moveType));
    }

    @OnEvent
    public void onMove(PlayerMovedEvent event) {
        this.location = event.cityId();
    }

    public CityId location() {
        return location;
    }
}
