package arollavengers.pattern.instrumenter;

import static arollavengers.pattern.func.Filters.and;
import static arollavengers.pattern.javassist.JavassistUtils.atLeastOneMethodParameterWithAnnotation;
import static arollavengers.pattern.javassist.JavassistUtils.findMethod;
import static arollavengers.pattern.javassist.JavassistUtils.findMethodInHierarchy;
import static arollavengers.pattern.javassist.JavassistUtils.sameMethodAs;

import arollavengers.pattern.javassist.JavassistUtils;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.annotation.Annotation;

import javax.annotation.Nonnull;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class NonnullInstrumenter implements Instrumenter {

    @Override
    public boolean instrument(CtClass clazz) throws CannotCompileException, NotFoundException {
        if (!canInstrument(clazz)) {
            return false;
        }

        boolean modified = false;
        for (CtMethod method : clazz.getDeclaredMethods()) {
            CtMethod annotatedMethod = findAnnotatedMethod(clazz, method);
            if (annotatedMethod != null) {
                modified = true;
                instrument(clazz, method, annotatedMethod);
            }
        }
        return modified;
    }

    private boolean canInstrument(CtClass clazz) {
        return !clazz.isInterface();
    }

    @Override
    public String getName() {
        return "NonnullParameter";
    }

    private CtMethod findAnnotatedMethod(CtClass ctClass, CtMethod method) throws NotFoundException {
        return findMethodInHierarchy(ctClass,
                and(sameMethodAs(method), atLeastOneMethodParameterWithAnnotation(Nonnull.class)));
    }

    protected void instrument(CtClass clazz, CtMethod method, CtMethod annotatedMethod)
            throws CannotCompileException, NotFoundException
    {
        Annotation[][] parametersAnnotations = JavassistUtils.getParametersAnnotations(annotatedMethod);
        if (parametersAnnotations == null)
        // should never happen due to filtering
        {
            return;
        }

        StringBuilder builder = new StringBuilder();
        CtClass[] parameterTypes = annotatedMethod.getParameterTypes();

        int parameterIndex = 0;
        for (Annotation[] parameterAnnotations : parametersAnnotations) {
            if (parameterAnnotations != null) {
                for (Annotation annotation : parameterAnnotations) {
                    if (annotation.getTypeName().equals(Nonnull.class.getName())
                            && !parameterTypes[parameterIndex].isPrimitive())
                    {
                        builder.append("if($" + (parameterIndex + 1)
                                + " == null) throw new IllegalArgumentException(\"Parameter cannot be null (index: "
                                + parameterIndex + ")\");");
                    }
                }
            }
            parameterIndex++;
        }

        method.insertBefore("{" + builder + "}");
    }
}
