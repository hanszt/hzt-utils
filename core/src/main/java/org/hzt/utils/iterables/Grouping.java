package org.hzt.utils.iterables;

import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.function.QuadFunction;
import org.hzt.utils.function.TriFunction;
import org.hzt.utils.iterators.Iterators;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

public interface Grouping<T, K> extends Iterable<T> {

    K keyOf(T element);

    default <R, M extends Map<K, R>> M aggregateTo(Supplier<? extends M> mapSupplier,
                                                   QuadFunction<? super K, ? super R, ? super T, Boolean, ? extends R> aggregator) {
        final M destination = mapSupplier.get();
        for (T item : this) {
            final K key = keyOf(item);
            final R accumulator = destination.get(key);
            final boolean isFirstValue = accumulator == null && !destination.containsKey(key);
            destination.put(key, aggregator.apply(key, accumulator, item, isFirstValue));
        }
        return destination;
    }


    default Grouping<T, K> filtering(Predicate<? super T> predicate) {
        return new Grouping<T, K>() {
            @Override
            public K keyOf(T element) {
                return Grouping.this.keyOf(element);
            }

            @Override
            public Iterator<T> iterator() {
                return Iterators.filteringIterator(Grouping.this.iterator(), predicate, true);
            }
        };
    }

    default <R> MapX<K, R> aggregate(QuadFunction<K, R, T, Boolean, R> aggregator) {
        return aggregateTo(MutableMapX::empty, aggregator);
    }

    default <R, M extends Map<K, R>> M foldTo(Supplier<M> mapSupplier,
                                              BiFunction<? super K, ? super T, ? extends R> initialValueSelector,
                                              TriFunction<? super K, ? super R, ? super T, ? extends R> operation) {
        return aggregateTo(mapSupplier, (key, acc, item, first) ->
                operation.apply(key, Boolean.TRUE.equals(first) ? initialValueSelector.apply(key, item) : acc, item));
    }

    default <R> MapX<K, R> fold(BiFunction<? super K, ? super T, ? extends R> initialValueSelector,
                                TriFunction<? super K, ? super R, ? super T, ? extends R> operation) {
        return foldTo(MutableMapX::empty, initialValueSelector, operation);
    }

    default <R, M extends Map<K, R>> M foldTo(Supplier<? extends M> mapSupplier,
                                              final R initialValue,
                                              BiFunction<? super R, ? super T, ? extends R> operation) {
        return aggregateTo(mapSupplier, (key, acc, item, first) ->
                operation.apply(Boolean.TRUE.equals(first) ? initialValue : acc, item));
    }

    default <R> MapX<K, R> fold(final R initialValue, BiFunction<? super R, ? super T, ? extends R> operation) {
        return foldTo(MutableMapX::empty, initialValue, operation);
    }

    default <R, A, M extends Map<K, R>> M collectTo(Supplier<M> mapSupplier,
                                                    Collector<? super T, A, R> downStream) {
        final M destination = mapSupplier.get();
        final Map<K, A> accumulators = new HashMap<>();
        for (T item : this) {
            final K key = keyOf(item);
            final A accumulator = accumulators.computeIfAbsent(key, k -> downStream.supplier().get());
            downStream.accumulator().accept(accumulator, item);
            destination.put(key, downStream.finisher().apply(accumulator));
        }
        return destination;
    }

    default <R, A> MapX<K, R> collect(Collector<? super T, A, R> collector) {
        return collectTo(MutableMapX::empty, collector);
    }

    default <M extends Map<K, T>> M reduceTo(Supplier<? extends M> mapSupplier,
                                             TriFunction<? super K, ? super T, ? super T, ? extends T> operation) {
        return aggregateTo(mapSupplier, (key, acc, item, first) ->
                Boolean.TRUE.equals(first) ? item : operation.apply(key, acc, item));
    }

    default MapX<K, T> reduce(TriFunction<? super K, ? super T, ? super T, ? extends T> operation) {
        return reduceTo(MutableMapX::empty, operation);
    }

    default <M extends Map<K, T>> M reduceTo(Supplier<? extends M> mapSupplier,
                                             BiFunction<? super T, ? super T, ? extends T> operation) {
        return reduceTo(mapSupplier, (key, acc, value) -> operation.apply(acc, value));
    }

    default MapX<K, T> reduce(BinaryOperator<T> operation) {
        return reduce((key, acc, value) -> operation.apply(acc, value));
    }

    default <M extends Map<K, Long>> M eachCountTo(Supplier<? extends M> mapSupplier) {
        return foldTo(mapSupplier, 0L, (acc, i) -> acc + 1);
    }

    default MutableMapX<K, Long> eachCount() {
        return eachCountTo(MutableMapX::empty);
    }
}
