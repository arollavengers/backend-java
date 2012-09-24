package arollavengers.pattern.instrumenter;

import static arollavengers.pattern.func.Filters.and;
import static arollavengers.pattern.javassist.JavassistUtils.findMethod;
import static arollavengers.pattern.javassist.JavassistUtils.findMethodInHierarchy;
import static arollavengers.pattern.javassist.JavassistUtils.methodWithAnnotation;
import static arollavengers.pattern.javassist.JavassistUtils.sameMethodAs;

import arollavengers.pattern.annotation.CanBeInvokedOnlyOnce;
import arollavengers.pattern.func.Filter;
import arollavengers.pattern.func.Filters;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CanBeInvokedOnlyOnceInstrumenter implements Instrumenter {

    private boolean verbose;

    @Override
    public boolean instrument(CtClass clazz) throws CannotCompileException, NotFoundException {
        if (!canInstrument(clazz)) {
            return false;
        }

        boolean modified = false;
        for (CtMethod method : clazz.getDeclaredMethods()) {
            if (mustBeInstrumented(clazz, method)) {
                modified = true;
                instrument(clazz, method);
            }
        }

        return modified;
    }

    private boolean canInstrument(CtClass clazz) {
        return !clazz.isInterface();
    }

    @Override
    public String getName() {
        return "CanBeInvokedOnlyOnce";
    }

    private boolean mustBeInstrumented(CtClass ctClass, CtMethod method) throws NotFoundException {
        // easy case
        if (method.hasAnnotation(CanBeInvokedOnlyOnce.class)) {
            return true;
        }

        Filter<CtMethod> filter = and(
                sameMethodAs(method),
                methodWithAnnotation(CanBeInvokedOnlyOnce.class));
        if (verbose) {
            filter = Filters.verbose(filter);
        }
        CtMethod found = findMethodInHierarchy(ctClass, filter);
        return found != null;
    }

    protected void instrument(CtClass clazz, CtMethod method) throws CannotCompileException {
        String fieldName = addFlagField(clazz, method);
        method.insertBefore("{" +
                "  if(" + fieldName + ")" +
                "    throw new arollavengers.pattern.annotation.CanBeInvokedOnlyOnce$MethodAlreadyInvokedException();" +
                "  " + fieldName + " = true;" +
                "}");
    }

    private String addFlagField(CtClass clazz, CtMethod method) throws CannotCompileException {
        String flagName = method.getName() + "_already_invoked";
        CtField flagField = new CtField(CtClass.booleanType, flagName, clazz);
        clazz.addField(flagField, "false");
        return flagName;
    }
}
