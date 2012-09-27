package arollavengers.core.domain.pandemic;

import arollavengers.core.exceptions.pandemic.InvalidPlayerCardUsageException;
import com.google.common.base.Optional;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class MoveService {
    private final CityStatesView cityStates;
    private final CityGraph cityGraph;

    public MoveService(CityStatesView cityStates, CityGraph cityGraph) {
        this.cityStates = cityStates;
        this.cityGraph = cityGraph;
    }

    public MoveType moveTypeFor(CityId cityFrom, CityId cityTo, Optional<PlayerCard> cardOpt) {
        // Drive
        if (cityGraph.areAdjacent(cityFrom, cityTo)) {
            return MoveType.Drive;
        }

        CityStateView cityFromView = cityStates.specializesViewFor(cityFrom);
        CityStateView cityToView = cityStates.specializesViewFor(cityTo);

        // Shuttle Flight
        // both have research center
        if (cityFromView.hasResearchCenter() && cityToView.hasResearchCenter()) {
            return MoveType.ShuttleFlight;
        }

        if (cardOpt.isPresent()) {
            PlayerCard playerCard = cardOpt.get();

            // TODO think of how to replace those nested switches
            // or this is simple enough to be kept as is
            //
            // maybe by something like
            //   MoveType moveType = card.moveTypeFor(cityFrom, cityTo);
            //
            // Furthermore 'MoveType.Airlift' could be replaced by
            // 'MoveType.SpecialCard' which is then more generic and doesn't
            // require to modify MoveType enum each time a new special card
            // is created

            switch (playerCard.cardType()) {
                case City:
                    CityId cityCard = ((PlayerCityCard) playerCard).cityId();
                    // Direct Flight
                    if(cityCard == cityFrom) {
                        return MoveType.DirectFlight;
                    }
                    // Charter Flight
                    else if(cityCard == cityTo) {
                        return MoveType.CharterFlight;
                    }
                    else {
                        throw new InvalidPlayerCardUsageException("Player card is not one of the from (" + cityFrom + ") or to (" + cityTo + ") city, got: " + playerCard);
                    }
                case SpecialEvent:
                    PlayerSpecialCard specialCard = (PlayerSpecialCard)playerCard;
                    if(specialCard==PlayerSpecialCard.Airlift) {
                        return MoveType.Airlift;
                    }
                    else {
                        throw new InvalidPlayerCardUsageException("Player card is not one of the from (" + cityFrom + ") or to (" + cityTo + ") city, got: " + playerCard);
                    }
                case Epidemic:
                default:
                    throw new InvalidPlayerCardUsageException(playerCard);
            }
        }

        return MoveType.None;
    }
}
