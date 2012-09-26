package arollavengers.core.domain.pandemic;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public enum InfectionRate {
    first(2),
    second(2),
    third(2),
    fourth(3),
    fifth(3),
    sixth(4),
    seventh(4);
/*
    eighth ,
    ninth ,
    tenth ,
    */

    private final int amount;

    InfectionRate(int amount) {
        this.amount = amount;
    }

    public int amount() {
        return amount;
    }

    public InfectionRate next() {
        InfectionRate[] values = values();
        int nextOrdinal = Math.min(ordinal() + 1, values.length - 1);
        return values[nextOrdinal];
    }

    @Override
    public String toString() {
        return name() + " (" + amount + ')';
    }
}
