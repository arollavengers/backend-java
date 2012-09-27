package arollavengers.core.domain.pandemic;

import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
public interface InfectionCard {
    InfectionCardType cardType();
}
