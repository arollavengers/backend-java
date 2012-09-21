package arollavengers.core.infrastructure;

import arollavengers.core.exceptions.InfrastructureRuntimeException;
import arollavengers.core.infrastructure.annotation.OnEvent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.jetbrains.annotations.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link EventHandler} that rely on the {@link OnEvent} annotation
 * to find the suitable method to dispatch the event.
 *
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 * @see OnEvent
 */
public class AnnotationBasedEventHandler<E> implements EventHandler<E> {

    private final Object target;

    /**
     * @param target the underlying target to which events will be dispatched.
     */
    public AnnotationBasedEventHandler(Object target) {
        this.target = target;
    }

    @Override
    public void handle(E event) {
        Method method = shared.getMethodFor(target.getClass(), event);
        try {
            method.invoke(target, event);
        }
        catch (IllegalAccessException e) {
            throw new OnEventMethodInvocationException(e);
        }
        catch (InvocationTargetException e) {
            throw new OnEventMethodInvocationException(e);
        }
    }


    private static Cache shared = new Cache();

    /**
     * Internal cache: class doesn't change at runtime, so let's cache the annotated method once.
     */
    private static class Cache {

        private final Map<Class<?>, List<Method>> methodsPerKlazz = Maps.newConcurrentMap();

        @NotNull
        private List<Method> getMethodsFor(Class<?> klazz) {
            List<Method> methods = methodsPerKlazz.get(klazz);
            if (methods == null) {
                methods = buildMethodsFor(klazz);
                methodsPerKlazz.put(klazz, methods);
            }
            return methods;
        }

        @NotNull
        private List<Method> buildMethodsFor(Class<?> klazz) {
            List<Method> methods =Lists.newArrayList();
            for (Method method : klazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(OnEvent.class)) {
                    if(method.getParameterTypes().length != 1) {
                        throw new InvalidOnEventMethodDefinitionException("Invalid number of parameters, expected: 1 but got: " + method.getParameterTypes().length + ", for method: " + method);
                    }
                    method.setAccessible(true);
                    methods.add(method);
                }
            }
            return methods;
        }

        @NotNull
        public Method getMethodFor(Class<?> aClass, Object event) {
            List<Method> methods = getMethodsFor(aClass);
            Class<?> eventClass = event.getClass();
            for(Method method : methods) {
                if(method.getParameterTypes()[0].isAssignableFrom(eventClass)) {
                    return method;
                }
            }
            throw new UndefinedOnEventMethodException("No annotated method found to dispatch event of type " + eventClass);
        }
    }

    public static class InvalidOnEventMethodDefinitionException extends InfrastructureRuntimeException {
        public InvalidOnEventMethodDefinitionException(String message) {
            super(message);
        }
    }

    public static class OnEventMethodInvocationException extends InfrastructureRuntimeException {
        public OnEventMethodInvocationException(Throwable cause) {
            super(cause);
        }
    }

    public static class UndefinedOnEventMethodException extends InfrastructureRuntimeException {
        public UndefinedOnEventMethodException(String message) {
            super(message);
        }
    }
}
