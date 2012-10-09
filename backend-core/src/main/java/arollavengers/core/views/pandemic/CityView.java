package arollavengers.core.views.pandemic;

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.domain.pandemic.Disease;
import arollavengers.core.exceptions.pandemic.PandemicRuntimeException;
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
@Table(name = "city_views")
public class CityView {

    public static PK pk(Id worldId, CityId cityId) {
        PK pk = new PK();
        pk.worldId = worldId.asString();
        pk.cityId = cityId.name();
        return pk;
    }

    @EmbeddedId
    private PK pk = new PK();

    @Column(name = "research_center")
    private boolean researchCenter;

    @Column(name = "nb_blue_cubes")
    private int nbBlueCubes;

    @Column(name = "nb_black_cubes")
    private int nbBlackCubes;

    @Column(name = "nb_orange_cubes")
    private int nbOrangeCubes;

    @Column(name = "nb_yellow_cubes")
    private int nbYellowCubes;

    /**
     * default constructor
     */
    public CityView() {
    }

    /**
     * @see #getWorldId()
     * @see #getCityId()
     */
    public void setId(Id worldId, CityId cityId) {
        this.pk.worldId = worldId.asString();
        this.pk.cityId = cityId.name();
    }

    public boolean hasResearchCenter() {
        return researchCenter;
    }

    public void setResearchCenter(boolean researchCenter) {
        this.researchCenter = researchCenter;
    }

    @Transient
    public int getNbCubes(Disease disease) {
        switch (disease) {
            case Black:
                return nbBlackCubes;
            case Yellow:
                return nbYellowCubes;
            case Orange:
                return
                        nbOrangeCubes;
            case Blue:
                return nbBlueCubes;
            default:
                throw new PandemicRuntimeException("Unsupported disease: " + disease);
        }
    }

    public void setNbCubes(Disease disease, int nbCubes) {
        switch (disease) {
            case Black:
                nbBlackCubes = nbCubes;
                break;
            case Yellow:
                nbYellowCubes = nbCubes;
                break;
            case Orange:
                nbOrangeCubes = nbCubes;
                break;
            case Blue:
                nbBlueCubes = nbCubes;
                break;
            default:
                throw new PandemicRuntimeException("Unsupported disease: " + disease);
        }
    }

    @Transient
    public Id getWorldId() {
        return Id.create(pk.worldId);
    }

    @Transient
    public CityId getCityId() {
        return CityId.valueOf(pk.cityId);
    }

    @Override
    public String toString() {
        return "CityView{" +
                "pk=" + pk +
                ", researchCenter=" + researchCenter +
                ", nbBlueCubes=" + nbBlueCubes +
                ", nbBlackCubes=" + nbBlackCubes +
                ", nbOrangeCubes=" + nbOrangeCubes +
                ", nbYellowCubes=" + nbYellowCubes +
                '}';
    }

    @Embeddable
    public static class PK implements Serializable {

        @Column(name = "world_id")
        public String worldId;

        @Column(name = "city_id")
        public String cityId;

        @Override
        public String toString() {
            return "PK{" +
                    "worldId='" + worldId + '\'' +
                    ", cityId='" + cityId + '\'' +
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
            return cityId.equals(pk.cityId) && worldId.equals(pk.worldId);
        }

        @Override
        public int hashCode() {
            int result = worldId.hashCode();
            result = 31 * result + cityId.hashCode();
            return result;
        }
    }
}
