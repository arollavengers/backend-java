package arollavengers.core.domain.pandemic;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface CityStatesView {
    CityStateView specializesViewFor(CityId cityId);
}
