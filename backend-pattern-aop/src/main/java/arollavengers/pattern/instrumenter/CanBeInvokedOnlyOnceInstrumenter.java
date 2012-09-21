package arollavengers.pattern.instrumenter;

import arollavengers.pattern.annotation.CanBeInvokedOnlyOnce;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CanBeInvokedOnlyOnceInstrumenter implements Instrumenter {

    @Override
    public boolean instrument(CtClass clazz) throws CannotCompileException, NotFoundException {
        if(!canInstrument(clazz))
            return false;

        boolean modified = false;
        for(CtMethod method : clazz.getDeclaredMethods()) {
            if(mustBeInstrumented(clazz, method)) {
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
        if(method.hasAnnotation(CanBeInvokedOnlyOnce.class))
            return true;

        return isMethodAnnotatedInHierarchy(ctClass, method);
    }

    private boolean isMethodAnnotatedInHierarchy(CtClass ctClass, CtMethod method) throws NotFoundException {
        CtMethod found = findMethod(ctClass, method);
        if(found!=null && found.hasAnnotation(CanBeInvokedOnlyOnce.class))
            return true;

        CtClass[] interfaces = ctClass.getInterfaces();
        if(interfaces!=null) {
            for(CtClass intf : interfaces) {
                boolean annotationPresent = isMethodAnnotatedInHierarchy(intf, method);
                if(annotationPresent)
                    return true;
            }
        }

        CtClass superclass = ctClass.getSuperclass();
        if(superclass != null) {
            return isMethodAnnotatedInHierarchy(superclass, method);
        }
        return false;
    }

    private static CtMethod findMethod(CtClass ctClass, CtMethod method) {
        for(CtMethod classMethod : ctClass.getDeclaredMethods()) {
            if(sameMethods(classMethod, method))
                return classMethod;
        }
        return null;
    }

    private static boolean sameMethods(CtMethod one, CtMethod two) {
        return two.getName().equals(one.getName()) && two.getSignature().equals(one.getSignature());
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
