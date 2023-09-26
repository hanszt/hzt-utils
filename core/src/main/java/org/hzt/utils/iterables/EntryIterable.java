package org.hzt.utils.iterables;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.collections.SetX;
import org.hzt.utils.collections.SortedMutableMapX;
import org.hzt.utils.sequences.Sequence;
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

public interface EntryIterable<K, V> extends Iterable<Map.Entry<K, V>> {

    <R> Iterable<R> map(@NotNull BiFunction<? super K, ? super V, ? extends R> biFunction);

    <K1, V1> EntryIterable<K1, V1> map(@NotNull Function<? super K, ? extends K1> keyMapper,
                                       @NotNull Function<? super V, ? extends V1> valueMapper);

    <K1, V1> EntryIterable<K1, V1> inverted(@NotNull Function<? super V, ? extends K1> toKeyMapper,
                                            @NotNull Function<? super K, ? extends V1> toValueMapper);

    EntryIterable<V, K> inverted();

    <K1> EntryIterable<K1, V> mapByKeys(@NotNull Function<? super K, ? extends K1> keyMapper);

    <K1> EntryIterable<K1, V> mapKeys(@NotNull BiFunction<? super K, ? super V, ? extends K1> toKeyMapper);

    <V1> EntryIterable<K, V1> mapByValues(@NotNull Function<? super V, ? extends V1> valueMapper);

    <V1> EntryIterable<K, V1> mapValues(@NotNull BiFunction<? super K, ? super V, ? extends V1> toValueMapper);

    EntryIterable<K, V> filter(@NotNull BiPredicate<? super K, ? super V> biPredicate);

    EntryIterable<K, V> filterKeys(@NotNull Predicate<? super K> predicate);

    EntryIterable<K, V> filterValues(@NotNull Predicate<? super V> predicate);

    EntryIterable<K, V> onEachKey(@NotNull Consumer<? super K> consumer);

    EntryIterable<K, V> onEachValue(@NotNull Consumer<? super V> consumer);

    EntryIterable<K, V> onEach(@NotNull BiConsumer<? super K, ? super V> biConsumer);

    void forEach(@NotNull BiConsumer<? super K, ? super V> biConsumer);

    default <R, C extends Collection<R>> C mapKeysTo(@NotNull final Supplier<C> collectionFactory,
                                                     @NotNull final Function<? super K, ? extends R> mapper) {
        return Sequence.of(this::keyIterator).mapTo(collectionFactory, mapper);
    }

    default <R, C extends Collection<R>> C mapValuesTo(@NotNull final Supplier<C> collectionFactory,
                                                       @NotNull final Function<? super V, ? extends R> mapper) {
        return Sequence.of(this::valueIterator).mapTo(collectionFactory, mapper);
    }

    default <R, C extends Collection<R>> C flatMapKeysTo(@NotNull final Supplier<C> collectionFactory,
                                                         @NotNull final Function<? super K, ? extends Iterable<? extends R>> mapper) {
        final var destination = collectionFactory.get();
        final Iterable<K> keyIterable = this::keyIterator;
        for (final var e : keyIterable) {
            final var iterable = mapper.apply(e);
            if (iterable instanceof Collection<?>) {
                //noinspection unchecked
                destination.addAll((Collection<R>) iterable);
            } else {
                iterable.forEach(destination::add);
            }
        }
        return destination;
    }

    default <R> ListX<R> flatMapKeys(@NotNull final Function<? super K, ? extends Iterable<? extends R>> mapper) {
        return flatMapKeysTo(MutableListX::empty, mapper);
    }

    default <R, C extends Collection<R>> C flatMapValuesTo(@NotNull final Supplier<C> collectionFactory,
                                                           @NotNull final Function<? super V, ? extends Iterable<? extends R>> mapper) {
        final var destination = collectionFactory.get();
        final Iterable<V> valueIterable = this::valueIterator;
        for (final var e : valueIterable) {
            final var iterable = mapper.apply(e);
            if (iterable instanceof Collection<?>) {
                //noinspection unchecked
                destination.addAll((Collection<R>) iterable);
            } else {
                iterable.forEach(destination::add);
            }
        }
        return destination;
    }

    default <R> ListX<R> flatMapValues(@NotNull final Function<? super V, ? extends Iterable<? extends R>> mapper) {
        return flatMapValuesTo(MutableListX::empty, mapper);
    }

    @NotNull
    default Iterator<V> valueIterator() {
        final var iterator = iterator();
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
        final var iterator = iterator();
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

    default long count(final BiPredicate<? super K, ? super V> predicate) {
        return Sequence.of(this).count(e -> predicate.test(e.getKey(), e.getValue()));
    }

    default MapX<K, V> toMapX() {
        return MapX.of(this);
    }

    default MutableMapX<K, V> toMutableMap() {
        return MutableMapX.of(this);
    }

    default Map<K, V> toMap() {
        return Map.copyOf(MutableMapX.of(this));
    }

    default <R extends Comparable<? super R>> SortedMutableMapX<K, V> toSortedMap(
            @NotNull final Function<? super K, ? extends R> selector) {
        return SortedMutableMapX.of(this, selector);
    }

    default <R> ListX<R> toListXOf(@NotNull final BiFunction<? super K, ? super V, ? extends R> transform) {
        return ListX.of(map(transform));
    }

    default <R> SetX<R> toSetXOf(@NotNull final BiFunction<? super K, ? super V, ? extends R> transform) {
        return Sequence.of(this).toSetXOf(e -> transform.apply(e.getKey(), e.getValue()));
    }

    default boolean any(final BiPredicate<K, V> biPredicate) {
        for (final var e : this) {
            if (biPredicate.test(e.getKey(), e.getValue())) {
                return true;
            }
        }
        return false;
    }

    default boolean all(final BiPredicate<K, V> biPredicate) {
        for (final var e : this) {
            if (!biPredicate.test(e.getKey(), e.getValue())) {
                return false;
            }
        }
        return true;
    }

    default boolean none(final BiPredicate<K, V> biPredicate) {
        for (final var e : this) {
            if (biPredicate.test(e.getKey(), e.getValue())) {
                return false;
            }
        }
        return true;
    }
}
