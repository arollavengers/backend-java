package arollavengers.core.domain.pandemic;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CityStateTest {

  private CityState state;

  @Before
  public void setUp () {
    state = new CityState();
  }

  @Test
  public void defaults() {
    for(Disease disease: Disease.values()) {
      assertThat(state.numberOfCubes(disease)).isEqualTo(0);
    }
  }

  @Test
  public void city_can_be_infected () {
    state.addCubes(Disease.Blue);
    assertThat(state.numberOfCubes(Disease.Blue)).isEqualTo(1);
    assertThat(state.numberOfCubes(Disease.Orange)).isEqualTo(0);
    assertThat(state.numberOfCubes(Disease.Black)).isEqualTo(0);
    assertThat(state.numberOfCubes(Disease.Yellow)).isEqualTo(0);

    state.addCubes(Disease.Blue);
    assertThat(state.numberOfCubes(Disease.Blue)).isEqualTo(2);
    assertThat(state.numberOfCubes(Disease.Orange)).isEqualTo(0);
    assertThat(state.numberOfCubes(Disease.Black)).isEqualTo(0);
    assertThat(state.numberOfCubes(Disease.Yellow)).isEqualTo(0);

    state.addCubes(Disease.Orange);
    assertThat(state.numberOfCubes(Disease.Blue)).isEqualTo(2);
    assertThat(state.numberOfCubes(Disease.Orange)).isEqualTo(1);
    assertThat(state.numberOfCubes(Disease.Black)).isEqualTo(0);
    assertThat(state.numberOfCubes(Disease.Yellow)).isEqualTo(0);
  }

  @Test(expected = IllegalStateException.class)
  public void city_cannot_be_treated_when_no_cube () {
    state.removeOneCube(Disease.Blue);
  }

  @Test
  public void city_can_be_treated_when_cubes_are_presents () {
    state.addCubes(Disease.Blue);
    state.addCubes(Disease.Blue);
    assertThat(state.numberOfCubes(Disease.Blue)).isEqualTo(2);

    state.removeOneCube(Disease.Blue);
    assertThat(state.numberOfCubes(Disease.Blue)).isEqualTo(1);
  }

}
