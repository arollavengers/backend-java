package arollavengers.core.service.time;

import org.springframework.stereotype.Service;

/**
 * {@link TimeService} implementation that relies on {@link System#currentTimeMillis()}.
 *
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 * @see TimeService
 * @see System#currentTimeMillis()
 */
@Service
public class SystemTimeService implements TimeService {

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

}
