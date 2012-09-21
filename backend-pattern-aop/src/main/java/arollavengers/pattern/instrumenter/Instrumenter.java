package arollavengers.pattern.instrumenter;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface Instrumenter {
    String getName();
    boolean instrument(CtClass clazz) throws CannotCompileException, NotFoundException;
}
