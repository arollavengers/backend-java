package arollavengers.core.infrastructure;

import arollavengers.core.util.Function;
import arollavengers.core.util.Functions;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Streams {

    public static <E> Stream<E> from(List<E> values) {
        return new StreamInMemory<E>(values);
    }

    public static <E> Stream<E> from(E... values) {
        return new StreamInMemory<E>(values);
    }

    public static <R, E> Stream<R> wrapAndCast(final Stream<E> source) {
        return new Stream<R>() {
            @Override
            public void consume(Function<R> function) {
                source.consume(Functions.<R, E>castAndForward(function));
            }

            @Override
            public boolean hasRemaining() {
                return source.hasRemaining();
            }
        };
    }

    public static <E> List<E> toList(Stream<E> stream) {
        final List<E> elements = Lists.newArrayList();
        stream.consume(new Function<E>() {
            @Override
            public void apply(E e) {
                elements.add(e);
            }
        });
        return elements;
    }
}
