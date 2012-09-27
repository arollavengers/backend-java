package arollavengers.core.exceptions.pandemic;

import arollavengers.core.domain.pandemic.PlayerCard;

import java.util.Arrays;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PlayerCardNotFoundException extends PandemicRuntimeException {
    public PlayerCardNotFoundException(PlayerCard card, PlayerCard... availables) {
        super("Card " + card + " not found among " + Arrays.toString(availables));
    }
}
