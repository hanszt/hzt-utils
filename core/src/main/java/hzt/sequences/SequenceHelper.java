package hzt.sequences;

import hzt.PreConditions;
import hzt.iterators.FilteringIterator;
import hzt.tuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Predicate;

public final class SequenceHelper {

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
        return new Iterator<R>() {
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

    public static void checkInitWindowSizeAndStep(int size, int step) {
        PreConditions.require(size > 0 && step > 0, () -> getErrorMessage(size, step));
    }

    static <T, A, R> Iterator<R> mergingIterator(@NotNull Iterator<T> thisIterator,
                                                 @NotNull Iterator<A> otherIterator,
                                                 @NotNull BiFunction<? super T, ? super A, ? extends R> transform) {
        return new Iterator<R>() {
            @Override
            public boolean hasNext() {
                return thisIterator.hasNext() && otherIterator.hasNext();
            }

            @Override
            public R next() {
                return transform.apply(thisIterator.next(), otherIterator.next());
            }
        };
    }

    @NotNull
    static <K, V> Iterator<Map.Entry<K, V>> toEntryIterator(final Iterator<Pair<K, V>> iterator) {
        return new Iterator<Map.Entry<K, V>>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Map.Entry<K, V> next() {
                final Pair<K, V> next = iterator.next();
                return new AbstractMap.SimpleEntry<>(next.first(), next.second());
            }
        };
    }

    @NotNull
    static  <T, K> Iterator<Pair<K, T>> associateByIterator(@NotNull Iterator<T> iterator,
                                                            @NotNull Function<? super T, ? extends K> keyMapper) {
        return new Iterator<Pair<K, T>>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public Pair<K, T> next() {
                final T value = iterator.next();
                return Pair.of(keyMapper.apply(value), value);
            }
        };
    }

    @NotNull
    static <T, V> Iterator<Pair<T, V>> associateWithIterator(@NotNull Iterator<T> iterator,
                                                             @NotNull Function<? super T, ? extends V> valueMapper) {
        return new Iterator<Pair<T, V>>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public Pair<T, V> next() {
                final T key = iterator.next();
                return Pair.of(key, valueMapper.apply(key));
            }
        };
    }

    private static String getErrorMessage(int size, int step) {
        if (size != step) {
            return "Both size " + size + " and step " + step + " must be greater than zero.";
        }
        return "size " + size + " must be greater than zero.";
    }

    static class HoldingConsumer implements IntConsumer {
        private int value = 0;

        @Override
        public void accept(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
