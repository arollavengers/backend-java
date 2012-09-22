package arollavengers.core.exceptions.pandemic;

import arollavengers.core.infrastructure.Id;

public class UserAlreadyRegisteredException extends PandemicRuntimeException {

    private final Id worldId;
    private final Id alreadyRegisteredUserId;

    public UserAlreadyRegisteredException(final Id worldId, final Id alreadyRegisteredUserId) {
        super("User with id " + alreadyRegisteredUserId + " is already registered on world with id " + worldId);
        this.worldId = worldId;
        this.alreadyRegisteredUserId = alreadyRegisteredUserId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final UserAlreadyRegisteredException that = (UserAlreadyRegisteredException) o;

        if (alreadyRegisteredUserId != null ? !alreadyRegisteredUserId.equals(that.alreadyRegisteredUserId) : that.alreadyRegisteredUserId != null)
            return false;
        if (worldId != null ? !worldId.equals(that.worldId) : that.worldId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = worldId != null ? worldId.hashCode() : 0;
        result = 31 * result + (alreadyRegisteredUserId != null ? alreadyRegisteredUserId.hashCode() : 0);
        return result;
    }
}
