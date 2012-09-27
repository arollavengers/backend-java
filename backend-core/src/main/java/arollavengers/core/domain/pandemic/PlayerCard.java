package arollavengers.core.domain.pandemic;

import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
public interface PlayerCard {
    PlayerCardType cardType();
}
