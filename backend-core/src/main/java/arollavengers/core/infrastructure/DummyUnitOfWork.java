package arollavengers.core.infrastructure;

import java.util.ArrayList;
import java.util.List;

public class DummyUnitOfWork implements UnitOfWork {

    private List<DomainEvent> uncommitted;

    public boolean commit() {
        //TODO c'est ici qu'il faut persister les events ?
        // ... ouep!
        uncommitted.clear();
        return true;
    }

    public boolean rollback() {
        //TODO comment faire un rollback puisque tous les évents ont déjà été
        //appliqués? Faut-il rejouer tous les événements commités dans d'autres uow ??
        throw new RuntimeException("not implemented");
    }

    public void registerNew(final DomainEvent event) {
        if (uncommitted == null) {
            uncommitted = new ArrayList<DomainEvent>();
        }
        uncommitted.add(event);
    }

    public List<DomainEvent> getUncommitted() {
        return uncommitted;
    }

    public void clearUncommitted() {
        uncommitted.clear();
    }
}
