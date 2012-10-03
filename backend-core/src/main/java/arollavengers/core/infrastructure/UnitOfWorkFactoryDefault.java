package arollavengers.core.infrastructure;

import org.springframework.stereotype.Component;
import javax.inject.Inject;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Component
public class UnitOfWorkFactoryDefault implements UnitOfWorkFactory {

    @Inject
    private Bus eventBus;

    @Override
    public UnitOfWork create() {
        return new UnitOfWorkDefault(eventBus);
    }
}
