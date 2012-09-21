package arollavengers.pattern.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates a method can only be invoked once.
 *
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
@Inherited
public @interface CanBeInvokedOnlyOnce {

    public static class MethodAlreadyInvokedException extends RuntimeException {}
}
