package org.hzt.utils.sequences;

import org.hzt.utils.It;
import org.hzt.utils.iterables.EntryIterable;
import org.hzt.utils.iterators.Iterators;
import org.hzt.utils.tuples.Pair;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
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
 * The implementation is heavily inspired on Kotlin's sequences api. This api offers simpler syntax than streams
 * and is easier to understand
 *
 * @param <K> the key type of the items in the Sequence
 * @param <V> the value type of the items in the Sequence
 */
public interface EntrySequence<K, V> extends Sequence<Map.Entry<K, V>>, EntryIterable<K, V> {

    static <K, V> EntrySequence<K, V> of(final Iterable<Map.Entry<K, V>> iterable) {
        return iterable::iterator;
    }

    static <K, V> EntrySequence<K, V> ofPairs(final Iterable<Pair<K, V>> pairIterable) {
        return () -> Iterators.transformingIterator(pairIterable.iterator(), e -> Map.entry(e.first(), e.second()));
    }

    static <K, V> EntrySequence<K, V> of(final Map<K, V> map) {
        return map.entrySet()::iterator;
    }

    default <R> Sequence<R> asSequence(final BiFunction<? super K, ? super V, ? extends R> biFunction) {
        return map(e -> biFunction.apply(e.getKey(), e.getValue()));
    }

    @Override
    default <K1, V1> EntrySequence<K1, V1> inverted(final Function<? super V, ? extends K1> toKeyMapper,
                                                    final Function<? super K, ? extends V1> toValueMapper) {
        return EntrySequence.of(map(e -> Map.entry(toKeyMapper.apply(e.getValue()), toValueMapper.apply(e.getKey()))));
    }

    @Override
    default EntrySequence<V, K> inverted() {
        return inverted(It::self, It::self);
    }

    default <R> Sequence<R> map(final BiFunction<? super K, ? super V, ? extends R> biFunction) {
        return map(e -> biFunction.apply(e.getKey(), e.getValue()));
    }

    default <K1, V1> EntrySequence<K1, V1> map(final Function<? super K, ? extends K1> keyMapper,
                                               final Function<? super V, ? extends V1> valueMapper) {
        return EntrySequence.of(map(e -> Map.entry(keyMapper.apply(e.getKey()), valueMapper.apply(e.getValue()))));
    }

    default <K1> EntrySequence<K1, V> mapByKeys(final Function<? super K, ? extends K1> keyMapper) {
        return EntrySequence.of(map(e -> Map.entry(keyMapper.apply(e.getKey()), e.getValue())));
    }

    @Override
    default <K1> EntrySequence<K1, V> mapKeys(final BiFunction<? super K, ? super V, ? extends K1> toKeyMapper) {
        return EntrySequence.of(map(e -> Map.entry(toKeyMapper.apply(e.getKey(), e.getValue()), e.getValue())));
    }

    default <V1> EntrySequence<K, V1> mapByValues(final Function<? super V, ? extends V1> valueMapper) {
        return EntrySequence.of(map(e -> Map.entry(e.getKey(), valueMapper.apply(e.getValue()))));
    }

    @Override
    default <V1> EntrySequence<K, V1> mapValues(final BiFunction<? super K, ? super V, ? extends V1> toValueMapper) {
        return EntrySequence.of(map(e -> Map.entry(e.getKey(), toValueMapper.apply(e.getKey(), e.getValue()))));
    }

    default EntrySequence<K, V> filter(final BiPredicate<? super K, ? super V> biPredicate) {
        return EntrySequence.of(Sequence.super.filter(e -> biPredicate.test(e.getKey(), e.getValue())));
    }

    default EntrySequence<K, V> filterKeys(final Predicate<? super K> predicate) {
        return EntrySequence.of(Sequence.super.filter(e -> predicate.test(e.getKey())));
    }

    default EntrySequence<K, V> filterValues(final Predicate<? super V> predicate) {
        return EntrySequence.of(Sequence.super.filter(e -> predicate.test(e.getValue())));
    }

    @Override
    default EntrySequence<K, V> onEachKey(final Consumer<? super K> consumer) {
        return mapByKeys(key -> {
            consumer.accept(key);
            return key;
        });
    }

    @Override
    default EntrySequence<K, V> onEachValue(final Consumer<? super V> consumer) {
        return EntrySequence.of(mapByValues(value -> {
            consumer.accept(value);
            return value;
        }));
    }

    @Override
    default EntrySequence<K, V> onEach(final BiConsumer<? super K, ? super V> biConsumer) {
        return EntrySequence.of(map(e -> {
            biConsumer.accept(e.getKey(), e.getValue());
            return e;
        }));
    }

    @Override
    default EntrySequence<K, V> onEach(final Consumer<? super Map.Entry<K, V>> consumer) {
        return EntrySequence.of(Sequence.super.onEach(consumer));
    }

    default void forEach(final BiConsumer<? super K, ? super V> biConsumer) {
        for (final var e : this) {
            biConsumer.accept(e.getKey(), e.getValue());
        }
    }

    default EntrySequence<K, V> skipWhileKeys(final Predicate<K> predicate) {
        return EntrySequence.of(Sequence.super.skipWhile(e -> predicate.test(e.getKey())));
    }

    default EntrySequence<K, V> skipWhileValues(final Predicate<V> predicate) {
        return EntrySequence.of(Sequence.super.skipWhile(e -> predicate.test(e.getValue())));
    }

    default EntrySequence<K, V> skipWhile(final BiPredicate<? super K, ? super V> predicate) {
        return () -> Iterators.skipWhileIterator(iterator(), e -> predicate.test(e.getKey(), e.getValue()), false);
    }

    default EntrySequence<K, V> skipWhileInclusive(final BiPredicate<? super K, ? super V> predicate) {
        return () -> Iterators.skipWhileIterator(iterator(), e -> predicate.test(e.getKey(), e.getValue()), true);
    }

    default EntrySequence<K, V> takeWhileKeys(final Predicate<K> predicate) {
        return () -> takeWhile(e -> predicate.test(e.getKey())).iterator();
    }

    default EntrySequence<K, V> takeWhileValues(final Predicate<V> predicate) {
        return () -> takeWhile(e -> predicate.test(e.getValue())).iterator();
    }

    default EntrySequence<K, V> takeWhile(final BiPredicate<? super K, ? super V> predicate) {
        return () -> Iterators.takeWhileIterator(iterator(), e -> predicate.test(e.getKey(), e.getValue()), false);
    }

    default EntrySequence<K, V> takeWhileInclusive(final BiPredicate<? super K, ? super V> predicate) {
        return () -> Iterators.takeWhileIterator(iterator(), e -> predicate.test(e.getKey(), e.getValue()), true);
    }

    @Override
    default EntrySequence<K, V> sorted(final Comparator<? super Map.Entry<K, V>> comparator) {
        return () -> Sequence.super.sorted(comparator).iterator();
    }

    @Override
    default <R extends Comparable<? super R>> EntrySequence<K, V> sortedBy(
            final Function<? super Map.Entry<K, V>, ? extends R> selector) {
        return () -> Sequence.super.sortedBy(selector).iterator();
    }

    @Override
    default <R> EntrySequence<K, V> distinctBy(final Function<? super Map.Entry<K, V>, ? extends R> selector) {
        return () -> Iterators.distinctIterator(iterator(), selector);
    }

    @Override
    default EntrySequence<K, V> skip(final long n) {
        return () -> Sequence.super.skip(n).iterator();
    }

    @Override
    default EntrySequence<K, V> take(final long n) {
        return () -> Sequence.super.take(n).iterator();
    }

    default EntrySequence<K, V> onEntrySequence(final Consumer<? super EntrySequence<K, V>> sequenceConsumer) {
        sequenceConsumer.accept(this);
        return this;
    }

    @Override
    default EntrySequence<K, V> constrainOnce() {
        final var consumed = new AtomicBoolean();
        return () -> Iterators.constrainOnceIterator(iterator(), consumed);
    }

    default Sequence<V> mergeKeys(final Function<? super K, ? extends V> toValueTypeMapper) {
        return flatMap(e -> Sequence.of(toValueTypeMapper.apply(e.getKey()), e.getValue()));
    }

    default Sequence<K> mergeValues(final Function<? super V, ? extends K> toKeyTypeMapper) {
        return flatMap(e -> Sequence.of(e.getKey(), toKeyTypeMapper.apply(e.getValue())));
    }
    default Sequence<V> merge() {
        return flatMap(e -> Sequence.of(keyAsValueTypeOrThrow(e), e.getValue()));
    }

    private V keyAsValueTypeOrThrow(final Map.Entry<K, V> entry) {
        final var k = entry.getKey();
        final var v = entry.getValue();
        if (k.getClass() == v.getClass()) {
            //noinspection unchecked
            return (V) k;
        }
        throw new IllegalStateException("Key and value not of same type. Merge not allowed");
    }

}
