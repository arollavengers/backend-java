package arollavengers.core.events;

import arollavengers.core.infrastructure.DomainEvent;
import arollavengers.core.infrastructure.UnitOfWork;

import java.util.ArrayList;
import java.util.List;

public class DummyUnitOfWork implements UnitOfWork<DomainEvent> {
  List<DomainEvent> uncommited;

  public boolean commit() {
    //TODO c'est ici qu'il faut persister les events ?
    throw new RuntimeException("not implemented");
  }

  public boolean rollback() {
    //TODO comment faire un rollback puisque tous les évents ont déjà été
    //appliqués? Faut-il rejouer tous les événements commités ??
    throw new RuntimeException("not implemented");
  }

  public void registerNew(final DomainEvent event) {
    if (uncommited == null) {
      uncommited = new ArrayList<DomainEvent>();
    }
    uncommited.add(event);
  }
}
