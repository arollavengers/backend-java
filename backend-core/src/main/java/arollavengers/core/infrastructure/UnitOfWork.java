package arollavengers.core.infrastructure;

public interface UnitOfWork {
  boolean commit();

  boolean rollback();

  void registerNew(DomainEvent event);
}
