package org.hzt.utils.iterables;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.collections.SetX;
import org.hzt.utils.collections.SortedMutableMapX;
import org.hzt.utils.sequences.Sequence;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface EntryIterable<K, V> extends Iterable<Map.Entry<K, V>> {

    <R> Iterable<R> map(BiFunction<? super K, ? super V, ? extends R> biFunction);

    <K1, V1> EntryIterable<K1, V1> map(Function<? super K, ? extends K1> keyMapper,
                                       Function<? super V, ? extends V1> valueMapper);

    <K1, V1> EntryIterable<K1, V1> inverted(Function<? super V, ? extends K1> toKeyMapper,
                                            Function<? super K, ? extends V1> toValueMapper);

    EntryIterable<V, K> inverted();

    <K1> EntryIterable<K1, V> mapByKeys(Function<? super K, ? extends K1> keyMapper);

    <K1> EntryIterable<K1, V> mapKeys(BiFunction<? super K, ? super V, ? extends K1> toKeyMapper);

    <V1> EntryIterable<K, V1> mapByValues(Function<? super V, ? extends V1> valueMapper);

    <V1> EntryIterable<K, V1> mapValues(BiFunction<? super K, ? super V, ? extends V1> toValueMapper);

    EntryIterable<K, V> filter(BiPredicate<? super K, ? super V> biPredicate);

    EntryIterable<K, V> filterKeys(Predicate<? super K> predicate);

    EntryIterable<K, V> filterValues(Predicate<? super V> predicate);

    EntryIterable<K, V> onEachKey(Consumer<? super K> consumer);

    EntryIterable<K, V> onEachValue(Consumer<? super V> consumer);

    EntryIterable<K, V> onEach(BiConsumer<? super K, ? super V> biConsumer);

    void forEach(BiConsumer<? super K, ? super V> biConsumer);

    default <R, C extends Collection<R>> C mapKeysTo(Supplier<C> collectionFactory,
                                                     Function<? super K, ? extends R> mapper) {
        return Sequence.of(this::keyIterator).mapTo(collectionFactory, mapper);
    }

    default <R, C extends Collection<R>> C mapValuesTo(Supplier<C> collectionFactory,
                                                       Function<? super V, ? extends R> mapper) {
        return Sequence.of(this::valueIterator).mapTo(collectionFactory, mapper);
    }

    default <R, C extends Collection<R>> C flatMapKeysTo(Supplier<C> collectionFactory,
                                                         Function<? super K, ? extends Iterable<? extends R>> mapper) {
        C destination = collectionFactory.get();
        final Iterable<K> keyIterable = this::keyIterator;
        for (K e : keyIterable) {
            Iterable<? extends R> iterable = mapper.apply(e);
            if (iterable instanceof Collection<?>) {
                //noinspection unchecked
                destination.addAll((Collection<R>) iterable);
            } else {
                iterable.forEach(destination::add);
            }
        }
        return destination;
    }

    default <R> ListX<R> flatMapKeys(Function<? super K, ? extends Iterable<? extends R>> mapper) {
        return flatMapKeysTo(MutableListX::empty, mapper);
    }

    default <R, C extends Collection<R>> C flatMapValuesTo(Supplier<C> collectionFactory,
                                                           Function<? super V, ? extends Iterable<? extends R>> mapper) {
        C destination = collectionFactory.get();
        final Iterable<V> valueIterable = this::valueIterator;
        for (V e : valueIterable) {
            Iterable<? extends R> iterable = mapper.apply(e);
            if (iterable instanceof Collection<?>) {
                //noinspection unchecked
                destination.addAll((Collection<R>) iterable);
            } else {
                iterable.forEach(destination::add);
            }
        }
        return destination;
    }

    default <R> ListX<R> flatMapValues(Function<? super V, ? extends Iterable<? extends R>> mapper) {
        return flatMapValuesTo(MutableListX::empty, mapper);
    }

    default Iterator<V> valueIterator() {
        Iterator<Map.Entry<K, V>> iterator = iterator();
        return new Iterator<V>() {
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

    default Iterator<K> keyIterator() {
        Iterator<Map.Entry<K, V>> iterator = iterator();
        return new Iterator<K>() {
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

    default long count(BiPredicate<? super K, ? super V> predicate) {
        return Sequence.of(this).count(e -> predicate.test(e.getKey(), e.getValue()));
    }

    default MapX<K, V> toMapX() {
        return MapX.of(this);
    }

    default MutableMapX<K, V> toMutableMap() {
        return MutableMapX.of(this);
    }

    default Map<K, V> toMap() {
        return Collections.unmodifiableMap(MutableMapX.of(this));
    }

    default <R extends Comparable<? super R>> SortedMutableMapX<K, V> toSortedMap(
            Function<? super K, ? extends R> selector) {
        return SortedMutableMapX.of(this, selector);
    }

    default <R> ListX<R> toListXOf(BiFunction<? super K, ? super V, ? extends R> transform) {
        return ListX.of(map(transform));
    }

    default <R> SetX<R> toSetXOf(BiFunction<? super K, ? super V, ? extends R> transform) {
        return Sequence.of(this).toSetXOf(e -> transform.apply(e.getKey(), e.getValue()));
    }

    default boolean any(BiPredicate<K, V> biPredicate) {
        for (Map.Entry<K, V> e : this) {
            if (biPredicate.test(e.getKey(), e.getValue())) {
                return true;
            }
        }
        return false;
    }

    default boolean all(BiPredicate<K, V> biPredicate) {
        for (Map.Entry<K, V> e : this) {
            if (!biPredicate.test(e.getKey(), e.getValue())) {
                return false;
            }
        }
        return true;
    }

    default boolean none(BiPredicate<K, V> biPredicate) {
        for (Map.Entry<K, V> e : this) {
            if (biPredicate.test(e.getKey(), e.getValue())) {
                return false;
            }
        }
        return true;
    }
}
