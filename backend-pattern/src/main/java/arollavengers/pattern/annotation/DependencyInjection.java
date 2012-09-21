package arollavengers.pattern.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks a method as part of the dependency injection.<br/>
 *
 * This may be used to denote setter usually created for testing purpose.
 *
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface DependencyInjection {
}
