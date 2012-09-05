package arollavengers.core.infrastructure;

import java.util.UUID;


public class Id {

  public static Id undefined() {
    return UNDEFINED_ID;
  }

  public static Id next() {
    return new Id(UUID.randomUUID());
  }

  private final UUID uuid;

  private Id(final UUID uuid) {
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
    return this.equals(Id.undefined());
  }

  private static final UndefinedId UNDEFINED_ID = new UndefinedId();
  private static class UndefinedId extends Id {
  @Override
    public String toString() {
      return "UndefinedId";
    }
  }
}


