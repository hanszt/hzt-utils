package hzt.sequences;

import hzt.tuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

final class SequenceHelper {

    private SequenceHelper() {
    }

    @NotNull
    static <T, K> Iterator<Pair<K, T>> associateByIterator(@NotNull Iterator<T> iterator,
                                                           @NotNull Function<? super T, ? extends K> valueMapper) {
        return new Iterator<Pair<K, T>>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public Pair<K, T> next() {
                final T value = iterator.next();
                final K key = valueMapper.apply(value);
                return Pair.of(key, value);
            }
        };
    }

    @NotNull
    static  <T, V> Iterator<Pair<T, V>> associateWithIterator(@NotNull Iterator<T> iterator,
                                                              @NotNull Function<? super T, ? extends V> valueMapper) {
        return new Iterator<Pair<T, V>>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public Pair<T, V> next() {
                final T key = iterator.next();
                final V value = valueMapper.apply(key);
                return Pair.of(key, value);
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
}
