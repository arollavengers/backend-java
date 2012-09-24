package arollavengers.pattern.func;

import javassist.CtMethod;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Filters {
    public static <T> Filter<T> and(final Filter<T> first, final Filter<T> second) {
        return new Filter<T>() {
            @Override
            public boolean isAccepted(T value) {
                return first.isAccepted(value) && second.isAccepted(value);
            }
        };
    }

    public static <T> Filter<T> verbose(final Filter<T> delegate) {
        return new Filter<T>() {
            @Override
            public boolean isAccepted(T value) {
                boolean accepted = delegate.isAccepted(value);
                System.out.println("Filters.isAccepted(" + toString(value) + ": " + accepted + ")");
                return accepted;
            }

            private String toString(T value) {
                if(value instanceof CtMethod) {
                    CtMethod m = (CtMethod)value;
                    return m.getDeclaringClass().getSimpleName()+"#"+m.getName();
                }
                return String.valueOf(value);
            }
        };
    }
}
