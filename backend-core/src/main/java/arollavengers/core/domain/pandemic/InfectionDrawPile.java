package arollavengers.core.domain.pandemic;

import arollavengers.core.events.pandemic.InfectionDrawPileCardDrawnEvent;
import arollavengers.core.events.pandemic.InfectionDrawPileInitializedEvent;
import arollavengers.core.events.pandemic.PlayerDrawPileCardDrawnEvent;
import arollavengers.core.events.pandemic.WorldEvent;
import arollavengers.core.exceptions.pandemic.PandemicRuntimeException;
import arollavengers.core.infrastructure.Aggregate;
import arollavengers.core.infrastructure.AnnotationBasedEventHandler;
import arollavengers.core.infrastructure.Entity;
import arollavengers.core.infrastructure.EventHandler;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.annotation.OnEvent;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class InfectionDrawPile extends Entity<WorldEvent> {

    private final EventHandler<WorldEvent> eventHandler;
    //
    private List<InfectionCard> cards;

    public InfectionDrawPile(@Nonnull Aggregate<WorldEvent> aggregate, @Nonnull Id entityId) {
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
     * @throws InfectionDrawPileAlreadyInitializedException
     *
     */
    public void initialize() throws InfectionDrawPileAlreadyInitializedException {
        ensureDrawPileNotAlreadyInitialized();

        // First collect all cards
        List<InfectionCard> cards = Lists.newArrayList();
        Collections.addAll(cards, InfectionCityCard.values());

        // then shuffle them
        Collections.shuffle(cards, new SecureRandom());

        applyNewEvent(new InfectionDrawPileInitializedEvent(entityId(), cards));
    }

    @OnEvent
    private void onPileInitialized(InfectionDrawPileInitializedEvent event) {
        this.cards = event.cards();
    }

    /**
     * Draw the first card on the top of the pile.
     *
     * @throws arollavengers.core.domain.pandemic.PlayerDrawPile.PlayerDrawPileNotYetInitializedException
     *
     */
    public InfectionCard drawTop() throws InfectionDrawPileNotYetInitializedException {
        ensureDrawPileHasBeenInitialized();

        InfectionCard card = cards.get(0);
        applyNewEvent(new InfectionDrawPileCardDrawnEvent(entityId(), card));
        return card;
    }

    @OnEvent
    private void onCardDrawn(InfectionDrawPileCardDrawnEvent event) {
        this.cards.remove(event.cardDrawn());
    }


    public int size() {
        return cards.size();
    }

    private void ensureDrawPileHasBeenInitialized() throws InfectionDrawPileNotYetInitializedException {
        if (cards == null) {
            throw new InfectionDrawPileNotYetInitializedException();
        }
    }

    private void ensureDrawPileNotAlreadyInitialized() throws InfectionDrawPileAlreadyInitializedException {
        if (cards != null) {
            throw new InfectionDrawPileAlreadyInitializedException();
        }
    }

    public static class InfectionDrawPileAlreadyInitializedException extends PandemicRuntimeException {
    }

    public static class InfectionDrawPileNotYetInitializedException extends PandemicRuntimeException {
    }

}
