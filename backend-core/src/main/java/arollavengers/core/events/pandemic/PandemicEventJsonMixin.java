package arollavengers.core.events.pandemic;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(CityInfectedEvent.class),
        @JsonSubTypes.Type(CurrentPlayerDefinedEvent.class),
        @JsonSubTypes.Type(FirstPlayerDesignatedEvent.class),
        @JsonSubTypes.Type(GameStartedEvent.class),
        @JsonSubTypes.Type(InfectionDrawPileCardDrawnEvent.class),
        @JsonSubTypes.Type(InfectionDrawPileCreatedEvent.class),
        @JsonSubTypes.Type(FirstPlayerDesignatedEvent.class),
        @JsonSubTypes.Type(FirstPlayerDesignatedEvent.class),
})
public interface PandemicEventJsonMixin {
}
