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
public class PlayerViewRepository {

    @PersistenceContext
    private EntityManager manager;

    @Transactional
    public void save(PlayerView view) {
        manager.persist(view);
    }

    @Transactional
    @Nullable
    public PlayerView find(PlayerView.PK id) {
        return manager.find(PlayerView.class, id);
    }

    @Transactional
    public void modifyOrNotFound(PlayerView.PK pk, ViewErrorHandler errorHandler, Function<PlayerView> updater) {
        PlayerView view = manager.find(PlayerView.class, pk);
        if(view==null)
            errorHandler.playerNotFound(pk);
        else
            updater.apply(view);
    }

    @Transactional
    public void forAll(Function<PlayerView> function) {
        for(Object row : manager.createQuery("from PlayerView").getResultList()) {
            function.apply((PlayerView)row);
        }
    }
}
