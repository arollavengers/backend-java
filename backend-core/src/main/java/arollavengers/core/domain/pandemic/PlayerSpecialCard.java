package arollavengers.core.domain.pandemic;

import java.util.EnumSet;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public enum PlayerSpecialCard implements PlayerCard {
    /**
     *
     */
    Epidemic,
    /**
     * Move a pawn (yours or another player's) to any city.
     * You must have a player's permission to move their pawn.
     */
    Airlift,
    /**
     * Examine the top 6 cards of the Infection Draw pile,
     * rearrange them in the order oy your choice, then place
     * them back on the pile.
     */
    Forecast,
    /**
     * Add a Research Station to any city for free.
     */
    GovernmentGrant,
    /**
     * The next player to begin the Playing The Infector phase
     * of their turn may skip that phase entirely.
     */
    OneQuietNight,
    /**
     * Take a card from the Infection Discard Pile and remove
     * it from the game.
     */
    ResilientPopulation;

    @Override
    public PlayerCardType cardType() {
        if(this==Epidemic)
            return PlayerCardType.Epidemic;
        return PlayerCardType.SpecialEvent;
    }

    public static EnumSet<PlayerSpecialCard> allExcept(PlayerSpecialCard excluded) {
        EnumSet<PlayerSpecialCard> cards = EnumSet.allOf(PlayerSpecialCard.class);
        cards.remove(excluded);
        return cards;
    }
}
