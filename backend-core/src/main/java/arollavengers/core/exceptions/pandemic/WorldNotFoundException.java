package arollavengers.core.exceptions.pandemic;

import arollavengers.core.infrastructure.Id;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class WorldNotFoundException extends PandemicRuntimeException {
    public WorldNotFoundException(Id worldId) {
        super("World with id " + worldId + " not found");
    }
}
