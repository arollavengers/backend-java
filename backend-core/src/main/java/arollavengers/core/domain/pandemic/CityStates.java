package arollavengers.core.domain.pandemic;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CityStates {
    private final Map<CityId, CityState> cityStates;

    public CityStates() {
        final HashMap<CityId, CityState> cityStatesSeed = Maps.newHashMap();
        for (CityId cityId : CityId.values()) {
            cityStatesSeed.put(cityId, new CityState());
        }
        this.cityStates = Collections.unmodifiableMap(cityStatesSeed);
    }

    public CityState getStateOf(@NotNull final CityId city) {
        return cityStates.get(city);
    }

    public int numberOfCubes(final Disease disease) {
        throw new RuntimeException("not implemented");
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

    public void buildResearchCenter(@NotNull final CityId city) {
        Preconditions.checkNotNull(city);
        getStateOf(city).buildResearchCenter();
    }
}
