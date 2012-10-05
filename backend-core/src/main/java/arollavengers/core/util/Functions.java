package arollavengers.core.util;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Functions {
    public static <R, E> Function<E> castAndForward(final Function<R> function) {
        return new Function<E>() {
            @SuppressWarnings("unchecked")
            @Override
            public void apply(E e) {
                function.apply((R) e);
            }
        };
    }

    public static <E> Function2<StringBuilder,E,StringBuilder> joinOp(final String separator) {
        return new Function2<StringBuilder, E, StringBuilder>() {
            @Override
            public StringBuilder apply(StringBuilder builder, E element) {
                if(builder.length()>0)
                    builder.append(separator);
                builder.append(element);
                return builder;
            }
        };
    }
}
