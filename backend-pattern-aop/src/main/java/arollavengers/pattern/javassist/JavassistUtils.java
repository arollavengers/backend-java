package arollavengers.pattern.javassist;

import arollavengers.pattern.func.Filter;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class JavassistUtils {


    /**
     * Indicate whether or not the same method is annotated within the class hierarchy.
     *
     * Search is perform recursively by looking at the interfaces first then to the superclass.
     *
     * @param ctClass root of the hierarchy tree
     * @param filter filter to search
     * @return <code>null</code> if none is found, else the first corresponding method
     * @throws NotFoundException
     */
    public static CtMethod findMethodInHierarchy(CtClass ctClass, Filter<CtMethod> filter) throws NotFoundException {
        CtMethod found = findMethod(ctClass, filter);
        if(found!=null && filter.isAccepted(found))
            return found;

        CtClass[] interfaces = ctClass.getInterfaces();
        if(interfaces!=null) {
            for(CtClass intf : interfaces) {
                found = findMethodInHierarchy(intf, filter);
                if(found!=null)
                    return found;
            }
        }

        CtClass superclass = ctClass.getSuperclass();
        if(superclass != null) {
            return findMethodInHierarchy(superclass, filter);
        }
        return null;
    }

    /**
     *
     */
    public static CtMethod findMethod(CtClass ctClass, Filter<CtMethod> filter) {
        for(CtMethod classMethod : ctClass.getDeclaredMethods()) {
            if(filter.isAccepted(classMethod))
                return classMethod;
        }
        return null;
    }

    public static Filter<CtMethod> atLeastOneMethodParameterWithAnnotation(final Class<? extends Annotation> annotationSearched) {
        return new Filter<CtMethod>() {
            @Override
            public boolean isAccepted(CtMethod value) {
                javassist.bytecode.annotation.Annotation[][] parametersAnnotations
                        = getParametersAnnotations(value);
                if(parametersAnnotations==null)
                    return false;
                for(javassist.bytecode.annotation.Annotation[] parameterAnnotations : parametersAnnotations) {
                    if(parameterAnnotations!=null) {
                        for(javassist.bytecode.annotation.Annotation annotation : parameterAnnotations) {
                            if(annotation.getTypeName().equals(annotationSearched.getName())) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        };
    }

    public static @Nullable
    javassist.bytecode.annotation.Annotation[][] getParametersAnnotations(CtMethod annotatedMethod) {
        MethodInfo methodInfo = annotatedMethod.getMethodInfo();
        ParameterAnnotationsAttribute attribute =
                (ParameterAnnotationsAttribute)methodInfo.getAttribute(ParameterAnnotationsAttribute.visibleTag);
        return attribute!=null?attribute.getAnnotations():null;
    }

    public static Filter<CtMethod> methodWithAnnotation(final Class<? extends Annotation> annotationSearched) {
        return new Filter<CtMethod>() {
            @Override
            public boolean isAccepted(CtMethod value) {
                return value.hasAnnotation(annotationSearched);
            }
        };
    }

    /**
     * Indicates if the two methods have the same name and the same signature.
     * This is useful while searching for both declaration and implementation methods.
     */
    public static boolean sameMethods(CtMethod one, CtMethod two) {
        return two.getName().equals(one.getName()) && two.getSignature().equals(one.getSignature());
    }

    public static Filter<CtMethod> sameMethodAs(final CtMethod ref) {
        return new Filter<CtMethod>() {
            @Override
            public boolean isAccepted(CtMethod method) {
                return sameMethods(ref, method);
            }
        };
    }
}
