package arollavengers.core.infrastructure;

import java.util.UUID;


public class Id {

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Id id = (Id) o;

        if (uuid != null ? !uuid.equals(id.uuid) : id.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    // ~~~ Undefined ID

    public static Id undefined() {
        return UNDEFINED_ID;
    }

    private static final UndefinedId UNDEFINED_ID = new UndefinedId();

    private static class UndefinedId extends Id {
        @Override
        public String toString() {
            return "UndefinedId";
        }
    }

    public boolean isUndefined() {
        return this.equals(Id.undefined());
    }
}


