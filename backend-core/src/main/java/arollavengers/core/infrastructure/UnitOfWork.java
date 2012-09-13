package arollavengers.core.infrastructure;

public interface UnitOfWork {
  void commit();

  void rollback();

  void registerNew(DomainEvent event);

  void registerEventStoreFor(Id aggregateId, EventStore eventStore);

  void attach(AggregateRoot<?> aggregateRoot);
  void detach(AggregateRoot<?> aggregateRoot);

  <T extends AggregateRoot<?>> T getAggregate(Id id);
}
