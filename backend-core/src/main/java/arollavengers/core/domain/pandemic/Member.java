package arollavengers.core.domain.pandemic;

import arollavengers.core.events.pandemic.PlayerCardDrawnFromPileEvent;
import arollavengers.core.events.pandemic.PlayerMovedEvent;
import arollavengers.core.events.pandemic.PlayerPositionOnTableDefinedEvent;
import arollavengers.core.events.pandemic.PlayerTurnEndedEvent;
import arollavengers.core.events.pandemic.PlayerTurnStartedEvent;
import arollavengers.core.events.pandemic.WorldEvent;
import arollavengers.core.exceptions.pandemic.ActionNotAuthorizedException;
import arollavengers.core.exceptions.pandemic.HandSizeLimitReachedException;
import arollavengers.core.exceptions.pandemic.PlayerCardNotFoundException;
import arollavengers.core.infrastructure.Aggregate;
import arollavengers.core.infrastructure.AnnotationBasedEventHandler;
import arollavengers.core.infrastructure.Entity;
import arollavengers.core.infrastructure.EventHandler;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.annotation.OnEvent;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.util.List;

public class Member extends Entity<WorldEvent> {

    public static final int HAND_SIZE_THRESHOLD = 7;
    //
    private final EventHandler<WorldEvent> eventHandler;
    //
    private final List<PlayerCard> hand = Lists.newArrayList();
    private final MemberKey memberKey;
    private int positionOnTable;
    private int nbActionRemaining;
    private CityId location;

    public Member(@Nonnull Aggregate<WorldEvent> aggregate,
                  @Nonnull Id entityId,
                  @Nonnull Id userId,
                  @Nonnull MemberRole role)
    {
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

    public void ensureActionIsAuthorized() throws ActionNotAuthorizedException {
        if (nbActionRemaining <= 0) {
            throw new ActionNotAuthorizedException(nbActionRemaining);
        }
    }

    public int handSize() {
        return hand.size();
    }

    /**
     *
     */
    public void addToHand(@Nonnull PlayerCard card) throws HandSizeLimitReachedException {
        if (handSize() >= HAND_SIZE_THRESHOLD) {
            throw new HandSizeLimitReachedException(entityId(), handSize());
        }
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

    public void startTurn(int nbActions) {
        applyNewEvent(new PlayerTurnStartedEvent(entityId(), nbActions));
    }

    @OnEvent
    private void onStartTurn(PlayerTurnStartedEvent event) {
        this.nbActionRemaining = event.nbActions();
    }

    public void endTurn() {
        applyNewEvent(new PlayerTurnEndedEvent(entityId()));
    }

    @OnEvent
    private void onEndTurn(PlayerTurnEndedEvent event) {
        this.nbActionRemaining = 0;
    }

    public void moveTo(@Nonnull CityId cityId, @Nonnull MoveType moveType, Optional<PlayerCard> cardOpt) {
        applyNewEvent(new PlayerMovedEvent(entityId(), cityId, moveType, cardOpt.orNull()));
    }

    @OnEvent
    public void onMove(PlayerMovedEvent event) {
        this.location = event.cityId();
        PlayerCard card = event.cardUsed();
        if (card != null) {
            hand.remove(card);
        }
    }

    public CityId location() {
        return location;
    }

    public void setPositionOnTable(int positionOnTable) {
        applyNewEvent(new PlayerPositionOnTableDefinedEvent(entityId(), positionOnTable));
    }

    @OnEvent
    private void onPositionOnTable(PlayerPositionOnTableDefinedEvent event) {
        this.positionOnTable = event.positionOnTable();
    }

    public int positionOnTable() {
        return positionOnTable;
    }

    public void ensurePlayerCardIsPresent(Optional<PlayerCard> cardOpt) throws PlayerCardNotFoundException {
        if (!cardOpt.isPresent()) {
            //nothing to check
            return;
        }
        PlayerCard card = cardOpt.get();
        if (!hand.contains(card)) {
            throw new PlayerCardNotFoundException(card, getHand());
        }
    }

    /**
     * Return a copy of the player's hand.
     */
    public PlayerCard[] getHand() {
        return hand.toArray(new PlayerCard[hand.size()]);
    }
}
