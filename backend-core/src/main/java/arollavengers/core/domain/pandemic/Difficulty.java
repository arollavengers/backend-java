package arollavengers.core.domain.pandemic;


public enum Difficulty {
  Introduction(4),
  Normal(5),
  Heroic(6);

  private final int nbEpidemicCards;

  Difficulty(final int nbEpidemicCards) {
    this.nbEpidemicCards = nbEpidemicCards;
  }

  public int nbEpidemicCards() {
    return nbEpidemicCards;
  }
}
