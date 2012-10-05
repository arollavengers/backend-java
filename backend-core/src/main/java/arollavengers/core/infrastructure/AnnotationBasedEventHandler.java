package arollavengers.core.infrastructure;

import arollavengers.core.exceptions.InfrastructureRuntimeException;
import arollavengers.core.infrastructure.annotation.OnEvent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    private final boolean failOnMissing;

    /**
     * @param target the underlying target to which events will be dispatched.
     */
    public AnnotationBasedEventHandler(Object target) {
        this(target, true);
    }

    /**
     * @param target        the underlying target to which events will be dispatched.
     * @param failOnMissing indicates if the handler should throw an exception
     *                      if no annotated method was found
     */
    public AnnotationBasedEventHandler(Object target, boolean failOnMissing) {
        this.target = target;
        this.failOnMissing = failOnMissing;
    }

    public boolean isFailOnMissing() {
        return failOnMissing;
    }

    @Override
    public void handle(E event, Object... args) {
        Method method = shared.getMethodFor(target.getClass(), event, args);
        try {
            if (method != null) {
                if (args != null && args.length>0) {
                    Object[] params = new Object[args.length + 1];
                    params[0] = event;
                    System.arraycopy(args, 0, params, 1, args.length);
                    method.invoke(target, params);
                }
                else {
                    method.invoke(target, event);
                }

            }
            else if (failOnMissing) {
                throw new UndefinedOnEventMethodException(
                        "No annotated method found to dispatch event of type " + event.getClass());
            }
        }
        catch (IllegalAccessException e) {
            throw new OnEventMethodInvocationException("Invoking method " +method.toString(), e);
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

        @Nonnull
        private List<Method> getMethodsFor(Class<?> klazz) {
            List<Method> methods = methodsPerKlazz.get(klazz);
            if (methods == null) {
                methods = buildMethodsFor(klazz);
                methodsPerKlazz.put(klazz, methods);
            }
            return methods;
        }

        @Nonnull
        private List<Method> buildMethodsFor(Class<?> klazz) {
            List<Method> methods = Lists.newArrayList();
            for (Method method : klazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(OnEvent.class)) {
                    if (method.getParameterTypes().length == 0) {
                        throw new InvalidOnEventMethodDefinitionException(
                                "Invalid number of parameters, expected: 1+ but got: "
                                        + method.getParameterTypes().length + ", for method: " + method);
                    }
                    method.setAccessible(true);
                    methods.add(method);
                }
            }
            return methods;
        }

        @Nullable
        public Method getMethodFor(Class<?> aClass, Object event, Object... params) {
            List<Method> methods = getMethodsFor(aClass);

            Class<?> eventClass = event.getClass();
            Method methodFoundWithoutArgument = null;
            methodLoop:
            for (Method method : methods) {
                if (!method.getParameterTypes()[0].isAssignableFrom(eventClass)) {
                    continue;
                }

                int methodNbParams = method.getParameterTypes().length;
                if (methodNbParams == params.length + 1) {
                    for (int i = 0; i < params.length; i++) {
                        if (!method.getParameterTypes()[i + 1].isAssignableFrom(params[i].getClass())) {
                            continue methodLoop;
                        }
                    }
                    return method;
                }
                else if(methodNbParams==1) {
                    methodFoundWithoutArgument = method;
                }
            }
            return methodFoundWithoutArgument;
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

        public OnEventMethodInvocationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class UndefinedOnEventMethodException extends InfrastructureRuntimeException {
        public UndefinedOnEventMethodException(String message) {
            super(message);
        }
    }
}
