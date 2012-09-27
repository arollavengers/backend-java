package arollavengers.core.domain.pandemic;

import static arollavengers.core.domain.pandemic.PlayerSpecialCard.Epidemic;

import arollavengers.core.events.pandemic.PlayerDrawPileCompletedForDifficultyEvent;
import arollavengers.core.events.pandemic.PlayerDrawPileInitializedEvent;
import arollavengers.core.events.pandemic.PlayerDrawPileCardDrawnEvent;
import arollavengers.core.events.pandemic.WorldEvent;
import arollavengers.core.exceptions.pandemic.PandemicRuntimeException;
import arollavengers.core.infrastructure.Aggregate;
import arollavengers.core.infrastructure.AnnotationBasedEventHandler;
import arollavengers.core.infrastructure.Entity;
import arollavengers.core.infrastructure.EventHandler;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.annotation.OnEvent;
import arollavengers.core.util.ListUtils;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PlayerDrawPile extends Entity<WorldEvent> {

    private final EventHandler<WorldEvent> eventHandler;
    //
    private List<PlayerCard> cards;

    public PlayerDrawPile(@Nonnull Aggregate<WorldEvent> aggregate, @Nonnull Id entityId) {
        super(aggregate, entityId);
        this.eventHandler = new AnnotationBasedEventHandler<WorldEvent>(this);
    }

    @Override
    protected EventHandler<WorldEvent> internalEventHandler() {
        return eventHandler;
    }

    /**
     * Initialize the pile and shuffle the cards in an unpredictible way.
     *
     * @throws arollavengers.core.domain.pandemic.PlayerDrawPile.PlayerDrawPileAlreadyInitializedException
     *
     */
    public void initialize() throws PlayerDrawPileAlreadyInitializedException {
        ensureDrawPileNotAlreadyInitialized();

        // First collect all cards
        List<PlayerCard> cards = Lists.newArrayList();
        Collections.addAll(cards, PlayerCityCard.values());
        cards.addAll(PlayerSpecialCard.allExcept(Epidemic));

        // then shuffle them
        Collections.shuffle(cards, new SecureRandom());

        applyNewEvent(new PlayerDrawPileInitializedEvent(entityId(), toArray(cards)));
    }

    private static PlayerCard[] toArray(List<PlayerCard> cards) {
        return cards.toArray(new PlayerCard[cards.size()]);
    }

    @OnEvent
    private void onPileInitialized(PlayerDrawPileInitializedEvent event) {
        this.cards = event.cards();
    }

    /**
     * Draw the first card on the top of the pile.
     *
     * @throws arollavengers.core.domain.pandemic.PlayerDrawPile.PlayerDrawPileNotYetInitializedException
     *
     */
    public PlayerCard drawTop() throws PlayerDrawPileNotYetInitializedException {
        ensureDrawPileHasBeenInitialized();

        PlayerCard card = cards.get(0);
        applyNewEvent(new PlayerDrawPileCardDrawnEvent(entityId(), card));
        return card;
    }

    @OnEvent
    private void onCardDrawn(PlayerDrawPileCardDrawnEvent event) {
        this.cards.remove(event.cardDrawn());
    }

    /**
     * Insert the {@link PlayerSpecialCard#Epidemic} cards according to the given difficulty.
     * {@link PlayerSpecialCard#Epidemic} cards are inserted in an unpredictable way into
     * predefined number of piles (see {@link arollavengers.core.domain.pandemic.Difficulty#nbEpidemicCards()}).
     *
     * @param difficulty difficulty used to adjust the player draw pile
     * @throws arollavengers.core.domain.pandemic.PlayerDrawPile.PlayerDrawPileNotYetInitializedException
     *
     * @throws arollavengers.core.domain.pandemic.PlayerDrawPile.PlayerDrawPileAlreadyAdjustedForDifficultyException
     *
     */
    public void completeForDifficulty(Difficulty difficulty) {
        ensureDrawPileHasBeenInitialized();
        ensureDrawPileNotAlreadyAdjustedForDifficulty();

        int nbEpidemicCards = difficulty.nbEpidemicCards();
        Random random = new SecureRandom();

        // Divide the remaining Player cards into 'nbEpidemicCards' *equal* (or at least close to) piles.
        List<List<PlayerCard>> listOfPile = ListUtils.split(cards, nbEpidemicCards);

        cards.clear();

        for (List<PlayerCard> pile : listOfPile) {
            // Shuffle 1 Epidemic card (face down) into each pile.
            int insertIndex = random.nextInt(pile.size());
            pile.add(insertIndex, PlayerSpecialCard.Epidemic);

            // Stack the piles together to form the Player Draw Pile.
            cards.addAll(pile);
        }
        applyNewEvent(new PlayerDrawPileCompletedForDifficultyEvent(entityId(), toArray(cards)));
    }

    @OnEvent
    private void onPileCompleted(PlayerDrawPileCompletedForDifficultyEvent event) {
        this.cards = event.cards();
    }

    public int size() {
        return cards.size();
    }

    private void ensureDrawPileHasBeenInitialized() throws PlayerDrawPileNotYetInitializedException {
        if (cards == null) {
            throw new PlayerDrawPileNotYetInitializedException();
        }
    }

    private void ensureDrawPileNotAlreadyInitialized() throws PlayerDrawPileAlreadyInitializedException {
        if (cards != null) {
            throw new PlayerDrawPileAlreadyInitializedException();
        }
    }

    private void ensureDrawPileNotAlreadyAdjustedForDifficulty() throws PlayerDrawPileNotYetInitializedException {
        if (cards.contains(PlayerSpecialCard.Epidemic)) {
            throw new PlayerDrawPileAlreadyAdjustedForDifficultyException();
        }
    }

    public static class PlayerDrawPileAlreadyInitializedException extends PandemicRuntimeException {
    }

    public static class PlayerDrawPileNotYetInitializedException extends PandemicRuntimeException {
    }

    public static class PlayerDrawPileAlreadyAdjustedForDifficultyException extends PandemicRuntimeException {
    }
}
