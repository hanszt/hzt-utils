package hzt.sequences;

import hzt.iterables.EntryIterable;
import hzt.tuples.Pair;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A sequence is a simplified stream. It evaluates its operations in a lazy way.
 * <p>
 * It does not support parallel execution.
 * <p>
 * The implementation is heavily inspired on Kotlin's sequences api. This api provides offers simpler syntax than streams
 * and is easier to understand
 *
 * @param <K> the key type of the items in the Sequence
 * @param <V> the value type of the items in the Sequence
 */
public interface EntrySequence<K, V> extends Sequence<Map.Entry<K, V>>, EntryIterable<K, V> {

    static <K, V> EntrySequence<K, V> of(Iterable<Map.Entry<K, V>> iterable) {
        return iterable::iterator;
    }

    static <K, V> EntrySequence<K, V> ofPairs(Iterable<Pair<K, V>> pairIterable) {
        return () -> toEntryIterator(pairIterable.iterator());
    }

    @NotNull
    private static <K, V> Iterator<Map.Entry<K, V>> toEntryIterator(final Iterator<Pair<K, V>> iterator) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Map.Entry<K, V> next() {
                final var next = iterator.next();
                return Map.entry(next.first(), next.second());
            }
        };
    }

    static <K, V> EntrySequence<K, V> of(Map<K, V> map) {
        return map.entrySet()::iterator;
    }

    default <R> Sequence<R> asSequence(BiFunction<K, V, R> biFunction) {
        return map(e -> biFunction.apply(e.getKey(), e.getValue()));
    }

    @Override
    default <K1, V1> EntrySequence<K1, V1> inverted(Function<K, V1> toValueMapper, Function<V, K1> toKeyMapper) {
        return EntrySequence.of(map(e -> Map.entry(toKeyMapper.apply(e.getValue()), toValueMapper.apply(e.getKey()))));
    }

    @Override
    default EntrySequence<V, K> inverted() {
        return inverted(It::self, It::self);
    }

    default <R> Sequence<R> map(@NotNull BiFunction<K, V, R> biFunction) {
        return map(e -> biFunction.apply(e.getKey(), e.getValue()));
    }

    default <K1, V1> EntrySequence<K1, V1> map(@NotNull Function<? super K, ? extends K1> keyMapper,
                                               @NotNull Function<? super V, ? extends V1> valueMapper) {
        return EntrySequence.of(map(e -> Map.entry(keyMapper.apply(e.getKey()), valueMapper.apply(e.getValue()))));
    }

    default <K1> EntrySequence<K1, V> mapKeys(@NotNull Function<? super K, ? extends K1> keyMapper) {
        return EntrySequence.of(map(e -> Map.entry(keyMapper.apply(e.getKey()), e.getValue())));
    }

    @Override
    default <K1> EntryIterable<K1, V> mapKeys(@NotNull BiFunction<? super K, ? super V, K1> toKeyMapper) {
        return EntrySequence.of(map(e -> Map.entry(toKeyMapper.apply(e.getKey(), e.getValue()), e.getValue())));
    }

    default <V1> EntrySequence<K, V1> mapValues(@NotNull Function<? super V, ? extends V1> valueMapper) {
        return EntrySequence.of(map(e -> Map.entry(e.getKey(), valueMapper.apply(e.getValue()))));
    }

    @Override
    default <V1> EntryIterable<K, V1> mapValues(@NotNull BiFunction<? super K, ? super V, V1> toValueMapper) {
        return EntrySequence.of(map(e -> Map.entry(e.getKey(), toValueMapper.apply(e.getKey(), e.getValue()))));
    }

    default EntrySequence<K, V> filter(@NotNull BiPredicate<K, V> biPredicate) {
        return EntrySequence.of(Sequence.super.filter(e -> biPredicate.test(e.getKey(), e.getValue())));
    }

    default EntrySequence<K, V> filterKeys(@NotNull Predicate<K> predicate) {
        return EntrySequence.of(Sequence.super.filter(e -> predicate.test(e.getKey())));
    }

    default EntrySequence<K, V> filterValues(@NotNull Predicate<V> predicate) {
        return EntrySequence.of(Sequence.super.filter(e -> predicate.test(e.getValue())));
    }

    @Override
    default EntrySequence<K, V> onEachKey(@NotNull Consumer<? super K> consumer) {
        return mapKeys(key -> {
            consumer.accept(key);
            return key;
        });
    }

    @Override
    default EntryIterable<K, V> onEachValue(@NotNull Consumer<? super V> consumer) {
        throw new UnsupportedOperationException();
    }

    @Override
    default EntryIterable<K, V> onEach(@NotNull BiConsumer<? super K, ? super V> biConsumer) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    default EntrySequence<K, V> onEach(@NotNull Consumer<? super Map.Entry<K, V>> consumer) {
        return EntrySequence.of(Sequence.super.onEach(consumer));
    }

    default void forEach(@NotNull BiConsumer<? super K, ? super V> biConsumer) {
        for (var e : this) {
            biConsumer.accept(e.getKey(), e.getValue());
        }
    }

    default EntrySequence<K, V> skipWhileKeys(Predicate<K> predicate) {
        return EntrySequence.of(Sequence.super.skipWhile(e -> predicate.test(e.getKey())));
    }

    default EntrySequence<K, V> skipWhileValues(Predicate<V> predicate) {
        return EntrySequence.of(Sequence.super.skipWhile(e -> predicate.test(e.getValue())));
    }

    default EntrySequence<K, V> takeWhileKeys(Predicate<K> predicate) {
        return EntrySequence.of(Sequence.super.takeWhile(e -> predicate.test(e.getKey())));
    }

    default EntrySequence<K, V> takeWhileValues(Predicate<V> predicate) {
        return EntrySequence.of(Sequence.super.takeWhile(e -> predicate.test(e.getValue())));
    }

    @Override
    default EntrySequence<K, V> skip(long n) {
        return EntrySequence.of(Sequence.super.skip(n));
    }

    @Override
    default EntrySequence<K, V> take(long n) {
        return EntrySequence.of(Sequence.super.take(n));
    }
}
