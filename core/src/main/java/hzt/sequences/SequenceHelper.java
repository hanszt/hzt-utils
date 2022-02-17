package hzt.sequences;

import hzt.iterators.FilteringIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

final class SequenceHelper {

    private SequenceHelper() {
    }

    static <T> Sequence<T> filteringSequence(Sequence<T> upstream, Predicate<T> predicate, boolean sendWhen) {
        return () -> FilteringIterator.of(upstream.iterator(), predicate, sendWhen);
    }

    static <T> Sequence<T> filteringSequence(Sequence<T> upstream, Predicate<T> predicate) {
        return filteringSequence(upstream, predicate, true);
    }

    static <T, R> Sequence<R> transformingSequence(Sequence<T> upstream, Function<? super T, ? extends R> mapper) {
        return () -> transformingIterator(upstream.iterator(), mapper);
    }

    private static <T, R> @NotNull Iterator<R> transformingIterator(Iterator<T> iterator, Function<? super T, ? extends R> mapper) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public R next() {
                return mapper.apply(iterator.next());
            }
        };
    }
}
