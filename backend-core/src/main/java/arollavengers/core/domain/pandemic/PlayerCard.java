package arollavengers.core.domain.pandemic;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlayerCityCard.class),
        @JsonSubTypes.Type(value = PlayerSpecialCard.class)
})
public interface PlayerCard {
    PlayerCardType cardType();
}
