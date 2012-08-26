package arollavengers.core.domain;


public enum Difficulty {
  Introduction(4),
  Normal(5),
  Heroic(6);

  private final int nbPiles;

  Difficulty(final int nbPiles) {
    this.nbPiles = nbPiles;
  }

  public int nbPiles() {
    return nbPiles;
  }
}
