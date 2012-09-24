package arollavengers.pattern.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation that marks a method as a candidate for <i>asynchronous</i> execution.
 *
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
@Inherited
public @interface Async {

    public interface AsyncCallback<E> {
        void onSucess(E what);
        void onError(E what, Exception e);
    }
}
