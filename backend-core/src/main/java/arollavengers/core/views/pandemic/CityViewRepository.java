package arollavengers.core.views.pandemic;

import arollavengers.core.util.Function;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Repository
public class CityViewRepository {

    @PersistenceContext
    private EntityManager manager;

    @Transactional
    public void save(CityView view) {
        manager.persist(view);
    }

    @Transactional
    @Nullable
    public CityView find(CityView.PK id) {
        return manager.find(CityView.class, id);
    }

    @Transactional
    public void modifyOrNotFound(CityView.PK pk, ViewErrorHandler errorHandler, Function<CityView> updater) {
        CityView view = manager.find(CityView.class, pk);
        if(view==null)
            errorHandler.cityNotFound(pk);
        else
            updater.apply(view);
    }
}
