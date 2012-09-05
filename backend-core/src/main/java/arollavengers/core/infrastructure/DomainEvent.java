package arollavengers.core.infrastructure;

public interface DomainEvent {
  long version();

  Id aggregateId();

  void assignVersion(long l);
}
