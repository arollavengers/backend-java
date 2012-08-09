package arollavengers.core.service.time;

/**
 *
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 * @see FrozenTimeService
 * @see SystemTimeService
 */
public interface TimeService {
    /**
     * Returns the current time in milliseconds.
     * @return the difference, measured in milliseconds, between
     *          the current time and midnight, January 1, 1970 UTC.
     */
    long currentTimeMillis();
}
