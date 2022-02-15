package hzt.iterables;

import hzt.collections.ListX;
import hzt.collections.MapX;
import hzt.collections.MutableMapX;
import hzt.collections.NavigableMapX;
import hzt.collections.SetX;
import hzt.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface EntryIterable<K, V> extends IterableX<Map.Entry<K, V>> {

    <R> IterableX<R> map(@NotNull BiFunction<K, V, R> biFunction);

    <K1, V1> EntryIterable<K1, V1> map(@NotNull Function<? super K, ? extends K1> keyMapper,
                                       @NotNull Function<? super V, ? extends V1> valueMapper);

    <K1, V1> EntryIterable<K1, V1> inverted(Function<K, V1> toValueMapper, Function<V, K1> toKeyMapper);

    EntryIterable<V, K> inverted();

    <K1> EntryIterable<K1, V> mapKeys(@NotNull Function<? super K, ? extends K1> keyMapper);

    <K1> EntryIterable<K1, V> mapKeys(@NotNull BiFunction<? super K, ? super V, K1> toKeyMapper);

    <V1> EntryIterable<K, V1> mapValues(@NotNull Function<? super V, ? extends V1> valueMapper);

    <V1> EntryIterable<K, V1> mapValues(@NotNull BiFunction<? super K, ? super V, V1> toValueMapper);

    EntryIterable<K, V> filter(@NotNull BiPredicate<K, V> biPredicate);

    EntryIterable<K, V> filterKeys(@NotNull Predicate<K> predicate);

    EntryIterable<K, V> filterValues(@NotNull Predicate<V> predicate);

    EntryIterable<K, V> onEachKey(@NotNull Consumer<? super K> consumer);

    EntryIterable<K, V> onEachValue(@NotNull Consumer<? super V> consumer);

    EntryIterable<K, V> onEach(@NotNull BiConsumer<? super K, ? super V> biConsumer);

    void forEach(@NotNull BiConsumer<? super K, ? super V> biConsumer);

    default <R, C extends Collection<R>> C mapKeysTo(Supplier<C> collectionFactory, Function<K, R> mapper) {
        return Sequence.of(this::keyIterator).mapTo(collectionFactory, mapper);
    }

    default <R, C extends Collection<R>> C mapValuesTo(Supplier<C> collectionFactory, Function<V, R> mapper) {
        return Sequence.of(this::valueIterator).mapTo(collectionFactory, mapper);
    }

    default <R, C extends Collection<R>> C flatMapKeysTo(Supplier<C> collectionFactory, Function<K, C> mapper) {
        C destination = collectionFactory.get();
        for (K e : (Iterable<K>) this::keyIterator) {
            C collection = mapper.apply(e);
            destination.addAll(collection);
        }
        return destination;
    }

    default <R, C extends Collection<R>> C flatMapValuesTo(Supplier<C> collectionFactory, Function<V, C> mapper) {
        C destination = collectionFactory.get();
        for (V e : (Iterable<V>) this::valueIterator) {
            C collection = mapper.apply(e);
            destination.addAll(collection);
        }
        return destination;
    }

    @NotNull
    default Iterator<V> valueIterator() {
        Iterator<Map.Entry<K, V>> iterator = iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public V next() {
                return iterator.next().getValue();
            }
        };
    }

    @NotNull
    default Iterator<K> keyIterator() {
        Iterator<Map.Entry<K, V>> iterator = iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public K next() {
                return iterator.next().getKey();
            }
        };
    }

    default long count(BiPredicate<K, V> predicate) {
        return IterableX.super.count(e -> predicate.test(e.getKey(), e.getValue()));
    }

    default MapX<K, V> toMapX() {
        return MutableMapX.of(this);
    }

    default MutableMapX<K, V> toMutableMap() {
        return MutableMapX.of(this);
    }

    default Map<K, V> toMap() {
        return Map.copyOf(MutableMapX.of(this));
    }

    default <R extends Comparable<R>> NavigableMapX<K, V> toSortedMapX(Function<K, R> selector) {
        return NavigableMapX.of(this, selector);
    }

    default <R> ListX<R> toListXOf(BiFunction<K, V, R> transform) {
        return ListX.of(map(e -> transform.apply(e.getKey(), e.getValue())));
    }

    default <R> SetX<R> toSetXOf(BiFunction<K, V, R> transform) {
        return toSetXOf(e -> transform.apply(e.getKey(), e.getValue()));
    }
}
