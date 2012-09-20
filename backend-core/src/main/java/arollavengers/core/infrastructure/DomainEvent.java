package arollavengers.core.infrastructure;

public interface DomainEvent extends Message {
  long version();

  Id aggregateId();

  void assignVersion(long l);
}
