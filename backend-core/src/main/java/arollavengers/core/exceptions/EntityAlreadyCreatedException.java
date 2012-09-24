package arollavengers.core.exceptions;

import arollavengers.core.infrastructure.Id;

public class EntityAlreadyCreatedException extends InfrastructureRuntimeException {
    private final Id entityId;

    public EntityAlreadyCreatedException(Id entityId) {
        super("Entity id " + entityId + " already created");
        this.entityId = entityId;
    }

    public Id getEntityId() {
        return entityId;
    }
}
