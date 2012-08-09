package arollavengers.core.service.time;

/**
 * {@link TimeService} implementation that returns a fixed value.
 *
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 * @see TimeService
 * @see #freezeTimeTo(long)
 */
public class FrozenTimeService implements TimeService {

    private long currentTimeMillis;

    public void freezeTimeTo(long currentTimeMillis) {
        this.currentTimeMillis = currentTimeMillis;
    }

    @Override
    public long currentTimeMillis() {
        return currentTimeMillis;
    }
}
