package arollavengers.core.domain.pandemic;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface CityStateView {
    int numberOfCubes(Disease disease);
    boolean hasResearchCenter();
}
