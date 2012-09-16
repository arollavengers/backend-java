package org.jbehave.core.steps;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class AnnotatedMethods {

    /**
     * Determines if the given type is a {@link Class} containing at least one method
     * annotated with annotations from package "org.jbehave.core.annotations".
     *
     * @param type the Type of the steps instance
     * @return A boolean, <code>true</code> if at least one annotated method is found.
     */
    public static boolean hasAnnotatedMethods(Type type) {
        if (type instanceof Class<?>) {
            Class<?> klazz = (Class<?>) type;
            return !recursivelyCollectAllAnnotatedMethods(klazz, true).isEmpty();
        }
        return false;
    }

    /**
     * Travers the given type hierarchy to collect all methods
     * annotated with annotations from package "org.jbehave.core.annotations".
     *
     * @param klazz the Type of the steps instance
     * @return all annotated methods found.
     *
     * @see #recursivelyCollectAllAnnotatedMethods(Class, boolean)
     */
    public static List<Method> recursivelyCollectAllAnnotatedMethods(Class<?> klazz) {
        return recursivelyCollectAllAnnotatedMethods(klazz, false);
    }

    /**
     * Travers the given type hierarchy to collect all methods
     * annotated with annotations from package "org.jbehave.core.annotations".
     *
     * @param klazz the Type of the steps instance
     * @param stopOnFirst indicates if the traversal should stop if at least one method
     *                    annotated with annotations from package "org.jbehave.core.annotations"
     *                    is found.
     * @return all annotated methods found.
     */
    public static List<Method> recursivelyCollectAllAnnotatedMethods(Class<?> klazz, boolean stopOnFirst) {
        List<Method> collected = new ArrayList<Method>(stopOnFirst?1:10);
        recursivelyCollectAllAnnotatedMethods(klazz, collected, false);
        return collected;
    }

    private static void recursivelyCollectAllAnnotatedMethods(Class<?> klazz, List<Method> collected, boolean stopOnFirst) {
        if(klazz.equals(Object.class)) {
            return;
        }

        for (Method method : klazz.getMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                if (annotation.annotationType().getName().startsWith(
                        "org.jbehave.core.annotations")) {
                    collected.add(method);
                    if(stopOnFirst)
                        return;
                }
            }
        }

        // interfaces?
        for(Class<?> intf : klazz.getInterfaces()) {
            recursivelyCollectAllAnnotatedMethods(intf, collected, stopOnFirst);
            if(stopOnFirst && !collected.isEmpty())
                return;
        }

        // time to check for parent
        Class<?> parent = klazz.getSuperclass();
        // interface does not have super class
        if(parent == null)
            return;

        while(!parent.equals(Object.class)) {
            recursivelyCollectAllAnnotatedMethods(parent, collected, stopOnFirst);
            if(stopOnFirst && !collected.isEmpty())
                return;
            parent = parent.getSuperclass();
        }
    }
}
