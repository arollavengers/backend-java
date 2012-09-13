package arollavengers.core.domain.pandemic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CityGraph {

  private List<Link> links = new ArrayList<Link>();
  private List<Link> unmodifiableRoutesView = Collections.unmodifiableList(links);

  /**
   * @return an unmodifiable view of all the links currently defined.
   */
  public List<Link> getLinks() {
    return unmodifiableRoutesView;
  }

  /**
   * Returns the list of the cities that are linked to the specified one.
   * @param cityId the city from which one wants to retrieve all the linked ones.
   * @return the list of the cities that are linked
   */
  public EnumSet<CityId> listCitiesLinkedTo(CityId cityId) {
    EnumSet<CityId> cityIds = EnumSet.noneOf(CityId.class);
    for (Link link : links) {
      if (link.contains(cityId)) {
        cityIds.add(link.other(cityId));
      }
    }
    return cityIds;
  }

  protected void initialize() {
    // ~~~ Blue
    define(CityId.SanFrancisco, CityId.Tokyo);
    define(CityId.SanFrancisco, CityId.Manila);
    define(CityId.SanFrancisco, CityId.LosAngeles);
    define(CityId.SanFrancisco, CityId.Chicago);
    define(CityId.Chicago, CityId.SanFrancisco);
    define(CityId.Chicago, CityId.LosAngeles);
    define(CityId.Chicago, CityId.MexicoCity);
    define(CityId.Chicago, CityId.Atlanta);
    define(CityId.Chicago, CityId.Toronto);
    define(CityId.Atlanta, CityId.Chicago);
    define(CityId.Atlanta, CityId.Miami);
    define(CityId.Atlanta, CityId.Washington);
    define(CityId.Toronto, CityId.Chicago);
    define(CityId.Toronto, CityId.Washington);
    define(CityId.Toronto, CityId.NewYork);
    define(CityId.Washington, CityId.NewYork);
    define(CityId.Washington, CityId.Toronto);
    define(CityId.Washington, CityId.Atlanta);
    define(CityId.Washington, CityId.Miami);
    define(CityId.NewYork, CityId.Toronto);
    define(CityId.NewYork, CityId.Washington);
    define(CityId.NewYork, CityId.Madrid);
    define(CityId.NewYork, CityId.London);
    define(CityId.Madrid, CityId.NewYork);
    define(CityId.Madrid, CityId.SaoPaulo);
    define(CityId.Madrid, CityId.Algiers);
    define(CityId.Madrid, CityId.Paris);
    define(CityId.Madrid, CityId.London);
    define(CityId.London, CityId.NewYork);
    define(CityId.London, CityId.Madrid);
    define(CityId.London, CityId.Paris);
    define(CityId.London, CityId.Essen);
    define(CityId.Paris, CityId.London);
    define(CityId.Paris, CityId.Madrid);
    define(CityId.Paris, CityId.Algiers);
    define(CityId.Paris, CityId.Milan);
    define(CityId.Paris, CityId.Essen);
    define(CityId.Essen, CityId.London);
    define(CityId.Essen, CityId.Paris);
    define(CityId.Essen, CityId.Milan);
    define(CityId.Essen, CityId.SaintPetersburg);
    define(CityId.Milan, CityId.Essen);
    define(CityId.Milan, CityId.Paris);
    define(CityId.Milan, CityId.Istanbul);
    define(CityId.SaintPetersburg, CityId.Essen);
    define(CityId.SaintPetersburg, CityId.Istanbul);
    define(CityId.SaintPetersburg, CityId.Moscow);
    // ~~~ Black
    // ~~~ Yellow
    // ~~~ Orange
  }


  private void define(CityId cityIdOne, CityId cityIdTwo) {
    Link r = new Link(cityIdOne, cityIdTwo);
    for (Link link : links) {
      if (link.sameAs(r)) {
        return;
      }
    }
    links.add(r);
  }

  /**
   * Describe a connection between two cities.
   */
  public static class Link {
    private final CityId cityIdOne;
    private final CityId cityIdTwo;

    public Link(CityId cityIdOne, CityId cityIdTwo) {
      this.cityIdOne = cityIdOne;
      this.cityIdTwo = cityIdTwo;
    }

    /**
     * Indicates whether or not this route is connected to the specified cityId.
     *
     * @param cityId the specified cityId
     * @return <code>true</code> if this route is connected to the specified cityId.
     */
    public boolean contains(CityId cityId) {
      return cityIdOne == cityId || cityIdTwo == cityId;
    }

    public boolean sameAs(Link link) {
      return (link.cityIdOne == cityIdOne && link.cityIdTwo == cityIdTwo)
              || (link.cityIdTwo == cityIdOne && link.cityIdOne == cityIdTwo);
    }

    @Override
    public String toString() {
      return "Link{" + cityIdOne + " <--> " + cityIdTwo + '}';
    }

    public CityId other(CityId cityId) {
      if (cityId == cityIdOne) {
        return cityIdTwo;
      }
      else {
        return cityIdOne;
      }
    }
  }
}
