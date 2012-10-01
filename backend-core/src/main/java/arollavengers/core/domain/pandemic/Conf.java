package arollavengers.core.domain.pandemic;

import arollavengers.pattern.annotation.ValueObject;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@ValueObject
public class Conf {

    public static Conf getDefault() {
        return builder()
                .usingMinTeamSize(2)
                .usingMaxTeamSize(4)
                .usingNbCubesOutbreakThreshold(4)
                .usingMaxPlayerHandSize(7)
                .usingNbPlayerCardsPerTurn(2)
                .usingNbPlayerActionsPerTurn(4)
                .build();
    }

    @JsonProperty
    private final int minTeamSize;

    @JsonProperty
    private final int maxTeamSize;

    @JsonProperty
    private final int nbPlayerActionsPerTurn;

    @JsonProperty
    private final int nbPlayerCardsPerTurn;

    @JsonProperty
    private final int maxPlayerHandSize;

    @JsonProperty
    private final int nbCubesOutbreakThreshold;

    @JsonCreator
    public Conf(@JsonProperty("minTeamSize") int minTeamSize,
                @JsonProperty("maxTeamSize") int maxTeamSize,
                @JsonProperty("nbPlayerActionsPerTurn") int nbPlayerActionsPerTurn,
                @JsonProperty("nbPlayerCardsPerTurn") int nbPlayerCardsPerTurn,
                @JsonProperty("maxPlayerHandSize") int maxPlayerHandSize,
                @JsonProperty("nbCubesOutbreakThreshold") int nbCubesOutbreakThreshold) {
        this.minTeamSize = minTeamSize;
        this.maxTeamSize = maxTeamSize;
        this.nbPlayerActionsPerTurn = nbPlayerActionsPerTurn;
        this.nbPlayerCardsPerTurn = nbPlayerCardsPerTurn;
        this.maxPlayerHandSize = maxPlayerHandSize;
        this.nbCubesOutbreakThreshold = nbCubesOutbreakThreshold;
    }

    public int minTeamSize() {
        return minTeamSize;
    }

    public int maxTeamSize() {
        return maxTeamSize;
    }

    public int nbPlayerActionsPerTurn() {
        return nbPlayerActionsPerTurn;
    }

    public int nbPlayerCardsPerTurn() {
        return nbPlayerCardsPerTurn;
    }

    public int maxPlayerHandSize() {
        return maxPlayerHandSize;
    }

    public int nbCubesOutbreakThreshold() {
        return nbCubesOutbreakThreshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Conf conf = (Conf) o;

        if (maxPlayerHandSize != conf.maxPlayerHandSize) {
            return false;
        }
        if (maxTeamSize != conf.maxTeamSize) {
            return false;
        }
        if (minTeamSize != conf.minTeamSize) {
            return false;
        }
        if (nbCubesOutbreakThreshold != conf.nbCubesOutbreakThreshold) {
            return false;
        }
        if (nbPlayerActionsPerTurn != conf.nbPlayerActionsPerTurn) {
            return false;
        }
        if (nbPlayerCardsPerTurn != conf.nbPlayerCardsPerTurn) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = minTeamSize;
        result = 31 * result + maxTeamSize;
        result = 31 * result + nbPlayerActionsPerTurn;
        result = 31 * result + nbPlayerCardsPerTurn;
        result = 31 * result + maxPlayerHandSize;
        result = 31 * result + nbCubesOutbreakThreshold;
        return result;
    }

    @Override
    public String toString() {
        return "Conf{" +
                "minTeamSize: " + minTeamSize +
                ", maxTeamSize: " + maxTeamSize +
                ", nbPlayerActionsPerTurn: " + nbPlayerActionsPerTurn +
                ", nbPlayerCardsPerTurn: " + nbPlayerCardsPerTurn +
                ", maxPlayerHandSize: " + maxPlayerHandSize +
                ", nbCubesOutbreakThreshold: " + nbCubesOutbreakThreshold +
                '}';
    }

    public Builder toBuilder() {
        return new Builder()
                .usingMinTeamSize(minTeamSize())
                .usingMaxTeamSize(maxTeamSize())
                .usingNbCubesOutbreakThreshold(nbCubesOutbreakThreshold())
                .usingMaxPlayerHandSize(maxPlayerHandSize())
                .usingNbPlayerCardsPerTurn(nbPlayerCardsPerTurn())
                .usingNbPlayerActionsPerTurn(nbPlayerActionsPerTurn());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int minTeamSize = 2;
        private int maxTeamSize = 4;
        private int nbPlayerActionsPerTurn = 4;
        private int nbPlayerCardsPerTurn = 2;
        private int maxPlayerHandSize = 7;
        private int nbCubesOutbreakThreshold = 4;

        public Builder usingMinTeamSize(int minTeamSize) {
            this.minTeamSize = minTeamSize;
            return this;
        }

        public Builder usingMaxTeamSize(int maxTeamSize) {
            this.maxTeamSize = maxTeamSize;
            return this;
        }

        public Builder usingNbCubesOutbreakThreshold(int nbCubesOutbreakThreshold) {
            this.nbCubesOutbreakThreshold = nbCubesOutbreakThreshold;
            return this;
        }

        public Builder usingMaxPlayerHandSize(int maxPlayerHandSize) {
            this.maxPlayerHandSize = maxPlayerHandSize;
            return this;
        }

        public Builder usingNbPlayerCardsPerTurn(int nbPlayerCardsPerTurn) {
            this.nbPlayerCardsPerTurn = nbPlayerCardsPerTurn;
            return this;
        }

        public Builder usingNbPlayerActionsPerTurn(int nbPlayerActionsPerTurn) {
            this.nbPlayerActionsPerTurn = nbPlayerActionsPerTurn;
            return this;
        }

        public Conf build() {
            return new Conf(minTeamSize, maxTeamSize, nbPlayerActionsPerTurn, nbPlayerCardsPerTurn, maxPlayerHandSize, nbCubesOutbreakThreshold);
        }
    }
}
