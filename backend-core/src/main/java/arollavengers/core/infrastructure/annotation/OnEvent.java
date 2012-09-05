package arollavengers.core.infrastructure.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks a method as a handler for a domain event.
 *
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface OnEvent {
}
