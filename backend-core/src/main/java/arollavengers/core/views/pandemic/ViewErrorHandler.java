package arollavengers.core.views.pandemic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Component
public class ViewErrorHandler {

    private Logger log = LoggerFactory.getLogger(ViewErrorHandler.class);

    private boolean addTraceWhenNotFound = true;

    public void setAddTraceWhenNotFound(boolean addTraceWhenNotFound) {
        this.addTraceWhenNotFound = addTraceWhenNotFound;
    }

    public void cityNotFound(CityView.PK pk) {
        if(addTraceWhenNotFound)
            log.warn("City not found: {}", pk, new Exception("<caller trace>"));
        else
            log.warn("City not found: {}", pk);
    }

    public void playerNotFound(PlayerView.PK pk) {
        if(addTraceWhenNotFound)
            log.warn("Player not found: {}", pk, new Exception("<caller trace>"));
        else
            log.warn("Player not found: {}", pk);
    }
}
