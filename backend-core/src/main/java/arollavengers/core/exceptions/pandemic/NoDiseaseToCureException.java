package arollavengers.core.exceptions.pandemic;

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.domain.pandemic.Disease;
import arollavengers.core.infrastructure.Id;

public class NoDiseaseToCureException extends RuntimeException {
  public NoDiseaseToCureException(final Id id, final CityId city, final Disease disease) {
  }
}
