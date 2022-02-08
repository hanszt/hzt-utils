package hzt.sequences;

import hzt.collections.ListX;
import hzt.collections.MapX;
import hzt.collections.MutableMapX;
import hzt.collections.SetX;
import hzt.iterables.EntryIterable;
import hzt.tuples.Pair;
import org.jetbrains.annotations.NotNull;

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
@SuppressWarnings("unused")
public interface EntrySequence<K, V> extends Sequence<Map.Entry<K, V>>, EntryIterable<K, V> {

    static <K, V> EntrySequence<K, V> of(Iterable<Map.Entry<K, V>> iterable) {
        return iterable::iterator;
    }

    default <R> Sequence<R> asSequence(BiFunction<K, V, R> biFunction) {
        return Sequence.super.map(e -> biFunction.apply(e.getKey(), e.getValue()));
    }

    default <K1, V1> EntrySequence<K1, V1> map(@NotNull BiFunction<K, V, Pair<K1, V1>> biFunction) {
        return EntrySequence.of(Sequence.super.map(e -> biFunction.apply(e.getKey(), e.getValue()).to(Map::entry)));
    }

    default <K1, V1> EntrySequence<K1, V1> map(@NotNull Function<K, K1> keyMapper, Function<V, V1> valueMapper) {
        return EntrySequence.of(Sequence.super.map(e -> Map.entry(keyMapper.apply(e.getKey()), valueMapper.apply(e.getValue()))));
    }

    default <K1> EntrySequence<K1, V> mapKeys(@NotNull Function<K, K1> keyMapper) {
        return EntrySequence.of(Sequence.super.map(e -> Map.entry(keyMapper.apply(e.getKey()), e.getValue())));
    }

    default <V1> EntrySequence<K, V1> mapValues(@NotNull Function<V, V1> valueMapper) {
        return EntrySequence.of(Sequence.super.map(e -> Map.entry(e.getKey(), valueMapper.apply(e.getValue()))));
    }

    default EntrySequence<K, V> filter(@NotNull BiPredicate<K, V> biPredicate) {
        return EntrySequence.of(Sequence.super.filter(e -> biPredicate.test(e.getKey(), e.getValue())));
    }

    default EntrySequence<K, V> filterByKeys(@NotNull Predicate<K> predicate) {
        return EntrySequence.of(Sequence.super.filter(e -> predicate.test(e.getKey())));
    }

    default EntrySequence<K, V> filterByValues(@NotNull Predicate<V> predicate) {
        return EntrySequence.of(Sequence.super.filter(e -> predicate.test(e.getValue())));
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

    default MutableMapX<K, V> toMutableMap() {
        return MutableMapX.ofEntries(this);
    }

    default <R> ListX<R> toListXOf(BiFunction<K, V, R> transform) {
        return map(e -> transform.apply(e.getKey(), e.getValue())).toListX();
    }

    default <R> SetX<R> toSetXOf(BiFunction<K, V, R> transform) {
        return toSetXOf(e -> transform.apply(e.getKey(), e.getValue()));
    }

    default MapX<K, V> toMapX() {
        return MutableMapX.ofEntries(this);
    }
}
