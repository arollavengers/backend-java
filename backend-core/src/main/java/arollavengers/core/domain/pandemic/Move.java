package arollavengers.core.domain.pandemic;

import com.google.common.base.Optional;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Move {

    public static Move drive(CityId cityFrom, CityId cityTo) {
        return new Move(MoveType.Drive, cityFrom, cityTo);
    }

    public static Move shuttleFlight(CityId cityFrom, CityId cityTo) {
        return new Move(MoveType.ShuttleFlight, cityFrom, cityTo);
    }

    public static Move directFlight(CityId cityFrom, CityId cityTo) {
        return new Move(MoveType.DirectFlight, cityFrom, cityTo);
    }

    public static Move charterFlight(CityId cityFrom, CityId cityTo) {
        return new Move(MoveType.CharterFlight, cityFrom, cityTo);
    }
    public static Move airlift(CityId cityFrom, CityId cityTo) {
        return new Move(MoveType.Airlift, cityFrom, cityTo);
    }

    private final MoveType moveType;
    private final CityId cityFrom;
    private final CityId cityTo;

    public Move(MoveType moveType, CityId cityFrom, CityId cityTo) {
        this.moveType = moveType;
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;
    }

}
