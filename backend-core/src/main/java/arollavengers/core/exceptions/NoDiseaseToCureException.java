package arollavengers.core.exceptions;

import arollavengers.core.domain.CityId;
import arollavengers.core.domain.Disease;
import arollavengers.core.infrastructure.Id;

public class NoDiseaseToCureException extends RuntimeException {
  public NoDiseaseToCureException(final Id id, final CityId city, final Disease disease) {
  }
}
