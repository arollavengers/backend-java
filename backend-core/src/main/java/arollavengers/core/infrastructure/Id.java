package arollavengers.core.infrastructure;

import arollavengers.core.pattern.annotation.ValueObject;

import java.util.UUID;

@ValueObject
public class Id {

  public static Id undefined() {
    return UNDEFINED_ID;
  }

  public static Id next() {
    return new Id(UUID.randomUUID().toString());
  }

  private final String uuid;

  private Id(final String uuid) {
    this.uuid = uuid;
  }

  private Id() {
    //only for UndefinedId instance
    this.uuid = null;
  }

  @Override
  public String toString() {
    if (uuid == null) {
      throw new NullPointerException("UUID is null, should not happen");
    }
    return uuid.toString();
  }

  public boolean isUndefined() {
    return this == UNDEFINED_ID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Id id = (Id) o;
    return uuid.equals(id.uuid);
  }

  @Override
  public int hashCode() {
    return uuid.hashCode();
  }

  private static final UndefinedId UNDEFINED_ID = new UndefinedId();

  public static Id create(String uuid) {
    return new Id(uuid);
  }

  private static class UndefinedId extends Id {
    @Override
    public String toString() {
      return "UndefinedId";
    }
  }
}


