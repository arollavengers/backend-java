package arollavengers.core.domain.pandemic;

import java.util.HashMap;
import java.util.Map;

public class CityState {

  private Map<Disease, Integer> cubes;

  public CityState() {
    cubes = new HashMap<Disease, Integer>();
    for (Disease disease : Disease.values()) {
      cubes.put(disease, 0);
    }
  }

  public int numberOfCubes(final Disease disease) {
    return cubes.get(disease);
  }

  public void removeOneCube(final Disease disease) {
    alreadyFreeOf(disease);
    cubes.put(disease, numberOfCubes(disease) - 1);
  }

  public void removeAllCubes(final Disease disease) {
    alreadyFreeOf(disease);
    cubes.put(disease, 0);
  }

  private void alreadyFreeOf(final Disease disease) {
    if (numberOfCubes(disease) == 0) {
      throw new IllegalStateException("City already free of " + disease);
    }
  }

  public void addCubes(Disease disease) {
    cubes.put(disease, numberOfCubes(disease) + 1);
  }
}
