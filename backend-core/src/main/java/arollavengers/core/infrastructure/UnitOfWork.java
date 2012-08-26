package arollavengers.core.infrastructure;

public interface UnitOfWork<E extends DomainEvent> {
  boolean commit();

  boolean rollback();

  void registerNew(E event);
}
