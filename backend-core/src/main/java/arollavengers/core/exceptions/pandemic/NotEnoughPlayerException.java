package arollavengers.core.exceptions.pandemic;

public class NotEnoughPlayerException extends RuntimeException {

    private final int teamSize;
    private final int minTeamSize;

    public NotEnoughPlayerException(final int teamSize, final int minTeamSize) {
        super("Game cannot start because there is not enougth player (" + teamSize + " on " + minTeamSize + ")");
        this.teamSize = teamSize;
        this.minTeamSize = minTeamSize;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final NotEnoughPlayerException that = (NotEnoughPlayerException) o;

        if (minTeamSize != that.minTeamSize) return false;
        if (teamSize != that.teamSize) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = teamSize;
        result = 31 * result + minTeamSize;
        return result;
    }
}
