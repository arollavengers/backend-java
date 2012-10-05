package arollavengers.core.util;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface Function2<InputType1,InputType2,Result> {
    Result apply(InputType1 value1, InputType2 value2);
}
