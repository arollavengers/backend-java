package arollavengers.core.views.pandemic;

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.infrastructure.Id;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Entity
@Table(name = "player_views")
public class PlayerView {

    public static PK pk(Id worldId, Id memberId) {
        PK pk = new PK();
        pk.worldId = worldId.asString();
        pk.playerId = memberId.asString();
        return pk;
    }

    @EmbeddedId
    private PK pk = new PK();

    @Column(name = "location")
    private String location;

    /**
     * @see #getWorldId()
     * @see #getPlayerId()
     */
    public void setId(Id worldId, Id memberId) {
        this.pk.worldId = worldId.asString();
        this.pk.playerId = memberId.asString();
    }

    @Transient
    public Id getWorldId() {
        return Id.create(pk.worldId);
    }

    @Transient
    public Id getPlayerId() {
        return Id.create(pk.playerId);
    }

    public void setLocation(CityId cityId) {
        this.location = cityId.name();
    }

    @Transient
    public CityId getLocation() {
        if(location==null)
            return CityId.Atlanta;
        return CityId.valueOf(location);
    }

    @Embeddable
    public static class PK implements Serializable {
        @Column(name = "world_id")
        public String worldId;
        @Column(name = "player_id")
        public String playerId;

        @Override
        public String toString() {
            return "PK{" +
                    "worldId='" + worldId + '\'' +
                    ", playerId='" + playerId + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            PK pk = (PK) o;

            if (!playerId.equals(pk.playerId)) {
                return false;
            }
            if (!worldId.equals(pk.worldId)) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = worldId.hashCode();
            result = 31 * result + playerId.hashCode();
            return result;
        }
    }
}
