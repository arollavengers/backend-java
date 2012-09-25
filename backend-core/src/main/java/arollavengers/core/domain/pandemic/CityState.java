package arollavengers.core.domain.pandemic;

import arollavengers.core.exceptions.pandemic.PandemicRuntimeException;
import com.google.common.base.Preconditions;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class CityState implements CityStateView {

    private EnumMap<Disease, Integer> cubes;

    private boolean researchCenter;

    public CityState() {
        Disease[] diseases = Disease.values();
        cubes = new EnumMap<Disease, Integer>(Disease.class);
        for (Disease disease : diseases) {
            cubes.put(disease, 0);
        }

        this.researchCenter = false;
    }

    @Override
    public int numberOfCubes(final Disease disease) {
        return cubes.get(disease);
    }

    public void removeAllCubes(final Disease disease) {
        alreadyFreeOf(disease);
        cubes.put(disease, 0);
    }

    public void removeOneCube(final Disease disease) {
        alreadyFreeOf(disease);
        cubes.put(disease, cubes.get(disease) - 1);
    }

    private void alreadyFreeOf(final Disease disease) {
        if (numberOfCubes(disease) == 0) {
            throw new PandemicRuntimeException("City already free of " + disease);
        }
    }

    @Override
    public boolean hasResearchCenter() {
        return researchCenter;
    }

    public void buildResearchCenter() {
        Preconditions.checkState(!hasResearchCenter());
        this.researchCenter = true;
    }

    public void addCubes(Disease disease) {
        cubes.put(disease, cubes.get(disease) + 1);
    }
}
