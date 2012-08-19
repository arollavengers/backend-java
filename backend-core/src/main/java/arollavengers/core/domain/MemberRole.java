package arollavengers.core.domain;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public enum MemberRole {
    /**
     * <ul>
     *     <li>Move your fellow players'pawns on your turn as if they were your own.</li>
     *     <li>Move any pawn to another city containing a pawn for one action</li>
     * </ul>
     */
    Dispatcher,
    /**
     * <ul>
     *     <li>Remove all cubes of a single color when you treat a city.</li>
     *     <li>Administer known cures for free</li>
     * </ul>
     */
    Medic,
    /**
     * <ul>
     *     <li>You may build a research station in your current city for one action.</li>
     * </ul>
     */
    OperationsExpert,
    /**
     * <ul>
     *     <li>You may give a player cards from your hand for one action per card.</li>
     *     <li>Both of your pawns must be in the same city, but it doesn't matter which city you are in.</li>
     * </ul>
     */
    Researcher,
    /**
     * <ul>
     *     <li>You need only 4 cards of the same color to discover a cure</li>
     * </ul>
     */
    Scientist;
}
