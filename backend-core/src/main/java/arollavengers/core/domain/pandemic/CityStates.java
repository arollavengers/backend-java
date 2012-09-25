package arollavengers.core.domain.pandemic;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import javax.annotation.Nonnull;

import java.util.*;

public class CityStates implements CityStatesView {
    private final EnumMap<CityId, CityState> cityStates;

    public CityStates() {
        this.cityStates = new EnumMap<CityId, CityState>(CityId.class);
        for (CityId cityId : CityId.values()) {
            cityStates.put(cityId, new CityState());
        }
    }

    public CityState getStateOf(@Nonnull final CityId city) {
        return cityStates.get(city);
    }

    public int numberOfCubes(final Disease disease) {
        int count = 0;
        for (CityState cityState : cityStates.values()) {
            count += cityState.numberOfCubes(disease);
        }
        return count;
    }

    @Override
    public CityStateView specializesViewFor(CityId cityId) {
        return cityStates.get(cityId);
    }

    public Collection<CityId> citiesWithResearchCenters() {
        Set<CityId> ret = Sets.newHashSet();
        for (CityId cityId : CityId.values()) {
            if (getStateOf(cityId).hasResearchCenter()) {
                ret.add(cityId);
            }
        }
        return ret;
    }

    public void buildResearchCenter(@Nonnull final CityId city) {
        Preconditions.checkNotNull(city);
        getStateOf(city).buildResearchCenter();
    }
}
