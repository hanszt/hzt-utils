package org.hzt.utils.iterables;

import org.hzt.utils.It;
import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableLinkedSetX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.collections.MutableSetX;
import org.hzt.utils.collections.SetX;
import org.hzt.utils.collections.primitives.DoubleMutableCollection;
import org.hzt.utils.collections.primitives.IntMutableCollection;
import org.hzt.utils.collections.primitives.LongMutableCollection;
import org.hzt.utils.function.IndexedFunction;
import org.hzt.utils.function.IndexedPredicate;
import org.hzt.utils.function.QuadFunction;
import org.hzt.utils.function.TriFunction;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.tuples.IndexedValue;
import org.hzt.utils.tuples.Pair;
import org.hzt.utils.tuples.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

@FunctionalInterface
public interface Collectable<T> extends IndexedIterable<T> {

    default T[] toTypedArray(final IntFunction<T[]> generator) {
        return toArrayOf(It::self, generator);
    }

    default <R> R[] toArrayOf(@NotNull final Function<? super T, ? extends R> mapper, @NotNull final IntFunction<R[]> generator) {
        final MutableListX<R> list = MutableListX.empty();
        for (final var t : this) {
            if (t != null) {
                list.add(mapper.apply(t));
            }
        }
        return list.toArray(generator);
    }

    default <K, V> MutableMapX<K, V> toMutableMap(@NotNull final Function<? super T, ? extends K> keyMapper,
                                                  @NotNull final Function<? super T, ? extends V> valueMapper) {
        final MutableMapX<K, V> map = MutableMapX.empty();
        for (final var t : this) {
            if (t != null) {
                final var key = keyMapper.apply(t);
                if (key != null) {
                    map.put(key, valueMapper.apply(t));
                }
            }
        }
        return map;
    }

    default <K, V> MapX<K, V> toMapX(@NotNull final Function<? super T, ? extends K> keyMapper,
                                     @NotNull final Function<? super T, ? extends V> valueMapper) {
        return toMutableMap(keyMapper, valueMapper);
    }

    default <K, V> Map<K, V> toMap(@NotNull final Function<? super T, ? extends K> keyMapper,
                                   @NotNull final Function<? super T, ? extends V> valueMapper) {
        return Map.copyOf(toMutableMap(keyMapper, valueMapper));
    }

    default <R, A> R collect(@NotNull final Collector<? super T, A, R> collector) {
        final var result = collector.supplier().get();
        final var accumulator = collector.accumulator();
        for (final var item : this) {
            if (item != null) {
                accumulator.accept(result, item);
            }
        }
        return collector.finisher().apply(result);
    }

    default <A, R> R collect(@NotNull final Supplier<A> supplier,
                             @NotNull final BiConsumer<A, ? super T> accumulator,
                             @NotNull final Function<A, R> finisher) {
        return collect(supplier, It::noFilter, It::self, It::noFilter, accumulator, finisher);
    }

    default <A, R> A collect(@NotNull final Supplier<A> supplier,
                             @NotNull final Predicate<T> filter,
                             @NotNull final Function<T, R> mapper,
                             @NotNull final BiConsumer<A, ? super R> accumulator) {
        return collect(supplier, filter, mapper, It::noFilter, accumulator, It::self);
    }

    default <A, U, R> R collect(@NotNull final Supplier<A> supplier,
                                @NotNull final Predicate<T> filter,
                                @NotNull final Function<T, U> mapper,
                                @NotNull final Predicate<U> resultFilter,
                                @NotNull final BiConsumer<A, ? super U> accumulator,
                                @NotNull final Function<A, R> finisher) {
        final var result = supplier.get();
        for (final var item : this) {
            if (filter.test(item)) {
                final var u = mapper.apply(item);
                if (resultFilter.test(u)) {
                    accumulator.accept(result, u);
                }
            }
        }
        return finisher.apply(result);
    }

    default <R1, A1, R2, A2, R> R teeing(@NotNull final Collector<? super T, A1, R1> downstream1,
                                 @NotNull final Collector<? super T, A2, R2> downstream2,
                                 @NotNull final BiFunction<? super R1, ? super R2, R> merger) {
        final var result1 = downstream1.supplier().get();
        final var result2 = downstream2.supplier().get();
        final var accumulator1 = downstream1.accumulator();
        final var accumulator2 = downstream2.accumulator();
        for (final var item : this) {
            if (item != null) {
                accumulator1.accept(result1, item);
                accumulator2.accept(result2, item);
            }
        }
        final var r1 = downstream1.finisher().apply(result1);
        final var r2 = downstream2.finisher().apply(result2);
        return merger.apply(r1, r2);
    }

    default <R1, R2> Pair<R1, R2> teeing(@NotNull final Collector<? super T, ?, R1> downstream1,
                                         @NotNull final Collector<? super T, ?, R2> downstream2) {
        return teeing(downstream1, downstream2, Pair::of);
    }

    default <R1, A1, R2, A2, R3, A3, R> R branching(@NotNull final Collector<? super T, A1, R1> downstream1,
                                        @NotNull final Collector<? super T, A2, R2> downstream2,
                                        @NotNull final Collector<? super T, A3, R3> downstream3,
                                        @NotNull final TriFunction<? super R1, ? super R2, ? super R3, R> merger) {
        final var result1 = downstream1.supplier().get();
        final var result2 = downstream2.supplier().get();
        final var result3 = downstream3.supplier().get();
        final var accumulator1 = downstream1.accumulator();
        final var accumulator2 = downstream2.accumulator();
        final var accumulator3 = downstream3.accumulator();
        for (final var item : this) {
            if (item != null) {
                accumulator1.accept(result1, item);
                accumulator2.accept(result2, item);
                accumulator3.accept(result3, item);
            }
        }
        final var r1 = downstream1.finisher().apply(result1);
        final var r2 = downstream2.finisher().apply(result2);
        final var r3 = downstream3.finisher().apply(result3);
        return merger.apply(r1, r2, r3);
    }

    default <R1, R2, R3> Triple<R1, R2, R3> branching(@NotNull final Collector<? super T, ?, R1> downstream1,
                                                      @NotNull final Collector<? super T, ?, R2> downstream2,
                                                      @NotNull final Collector<? super T, ?, R3> downstream3) {
        return branching(downstream1, downstream2, downstream3, Triple::of);
    }

    default <A1, R1, A2, R2, A3, R3, A4, R4, R> R branching(@NotNull final Collector<? super T, A1, R1> downstream1,
                                        @NotNull final Collector<? super T, A2, R2> downstream2,
                                        @NotNull final Collector<? super T, A3, R3> downstream3,
                                        @NotNull final Collector<? super T, A4, R4> downstream4,
                                        @NotNull final QuadFunction<? super R1, ? super R2, ? super R3, ? super R4, R> merger) {
        final var result1 = downstream1.supplier().get();
        final var result2 = downstream2.supplier().get();
        final var result3 = downstream3.supplier().get();
        final var result4 = downstream4.supplier().get();
        final var accumulator1 = downstream1.accumulator();
        final var accumulator2 = downstream2.accumulator();
        final var accumulator3 = downstream3.accumulator();
        final var accumulator4 = downstream4.accumulator();
        for (final var item : this) {
            if (item != null) {
                accumulator1.accept(result1, item);
                accumulator2.accept(result2, item);
                accumulator3.accept(result3, item);
                accumulator4.accept(result4, item);
            }
        }
        final var r1 = downstream1.finisher().apply(result1);
        final var r2 = downstream2.finisher().apply(result2);
        final var r3 = downstream3.finisher().apply(result3);
        final var r4 = downstream4.finisher().apply(result4);
        return merger.apply(r1, r2, r3, r4);
    }

    default <C extends Collection<T>> C to(final Supplier<C> collectionFactory) {
        return IterableXHelper.mapFilteringTo(this, collectionFactory, It::noFilter, It::self, It::noFilter);
    }

    default MutableListX<T> toMutableList() {
        return to(MutableListX::empty);
    }

    default ListX<T> toListX() {
        return ListX.copyOf(toMutableList());
    }

    default List<T> toList() {
        return List.copyOf(toMutableList());
    }

    default <R> List<R> toListOf(@NotNull final Function<? super T, ? extends R> transform) {
        return List.copyOf(mapNotNullTo(MutableListX::empty, transform));
    }

    default MutableSetX<T> toMutableSet() {
        return IterableXHelper.mapFilteringTo(this, MutableSetX::empty, Objects::nonNull, It::self, It::noFilter);
    }

    default SetX<T> toSetX() {
        return SetX.copyOf(toMutableSet());
    }

    default <R> SetX<R> toSetXOf(@NotNull final Function<? super T, ? extends R> transform) {
        return mapNotNullTo(MutableSetX::empty, transform);
    }

    default Set<T> toSet() {
        return Set.copyOf(toMutableSet());
    }

    default <R> Set<R> toSetOf(@NotNull final Function<? super T, ? extends R> transform) {
        return Collections.unmodifiableSet(this.<R, MutableSetX<R>>mapNotNullTo(MutableSetX::empty, transform));
    }

    default <R, C extends Collection<R>> C mapTo(@NotNull final Supplier<C> collectionFactory,
                                                 @NotNull final Function<? super T, ? extends R> mapper) {
        return IterableXHelper.mapFilteringTo(this, collectionFactory, Objects::nonNull, mapper, It::noFilter);
    }
    default <R, C extends Collection<R>> C mapNotNullTo(@NotNull final Supplier<C> collectionFactory,
                                                        @NotNull final Function<? super T, ? extends R> mapper) {
        return IterableXHelper.mapFilteringTo(this, collectionFactory, Objects::nonNull, mapper, Objects::nonNull);
    }

    default <R, C extends Collection<R>> C mapIfPresentTo(@NotNull final Supplier<C> collectionFactory,
                                                          @NotNull final Function<? super T, Optional<R>> mapper) {
        final var collection = collectionFactory.get();
        for (final var t : this) {
            if (t != null) {
                mapper.apply(t).ifPresent(collection::add);
            }
        }
        return collection;
    }

    default <R, C extends Collection<R>> C mapIndexedTo(@NotNull final Supplier<C> collectionFactory,
                                                        @NotNull final IndexedFunction<? super T, ? extends R> mapper) {
        final var collection = collectionFactory.get();
        var index = 0;
        for (final var value : this) {
            collection.add(mapper.apply(index, value));
            index++;
        }
        return collection;
    }

    default <R, I extends Iterable<? extends R>, C extends Collection<R>> C flatMapTo(
            @NotNull final Supplier<C> collectionSupplier,
            @NotNull final Function<? super T, ? extends I> mapper) {
        final var collection = collectionSupplier.get();
        for (final var item : this) {
            final var c = mapper.apply(item);
            if (c == null) {
                continue;
            }
            for (final R r : c) {
                if (r != null) {
                    collection.add(r);
                }
            }
        }
        return collection;
    }

    default <I extends PrimitiveIterable.OfInt, C extends IntMutableCollection> C flatMapIntsTo(
            @NotNull final Supplier<C> collectionSupplier,
            @NotNull final Function<? super T, ? extends I> mapper) {
        final var collection = collectionSupplier.get();
        for (final var item : this) {
            final var c = mapper.apply(item);
            if (c == null) {
                continue;
            }
            final var iterator = c.iterator();
            while (iterator.hasNext()) {
                collection.add(iterator.nextInt());
            }
        }
        return collection;
    }

    default <I extends PrimitiveIterable.OfLong, C extends LongMutableCollection> C flatMapLongsTo(
            @NotNull final Supplier<C> collectionSupplier,
            @NotNull final Function<? super T, ? extends I> mapper) {
        final var collection = collectionSupplier.get();
        for (final var item : this) {
            final var c = mapper.apply(item);
            if (c == null) {
                continue;
            }
            final var iterator = c.iterator();
            while (iterator.hasNext()) {
                collection.add(iterator.nextLong());
            }
        }
        return collection;
    }

    default <I extends PrimitiveIterable.OfDouble, C extends DoubleMutableCollection> C flatMapDoublesTo(
            @NotNull final Supplier<C> collectionSupplier,
            @NotNull final Function<? super T, ? extends I> mapper) {
        final var collection = collectionSupplier.get();
        for (final var item : this) {
            final var c = mapper.apply(item);
            if (c != null) {
                final var iterator = c.iterator();
                while (iterator.hasNext()) {
                    collection.add(iterator.nextDouble());
                }
            }
        }
        return collection;
    }

    default <R, C extends Collection<R>> C mapMultiTo(
            @NotNull final Supplier<C> collectionSupplier,
            @NotNull final BiConsumer<? super T, ? super Consumer<R>> mapper) {
        final var collection = collectionSupplier.get();
        for (final var item : this) {
            mapper.accept(item, (Consumer<R>) collection::add);
        }
        return collection;
    }

    default <C extends Collection<T>> C filterTo(@NotNull final Supplier<C> collectionFactory,
                                                 @NotNull final Predicate<? super T> predicate) {
        return IterableXHelper.mapFilteringTo(this, collectionFactory, predicate, It::self, It::noFilter);
    }

    default <C extends Collection<T>> C filterNotTo(@NotNull final Supplier<C> collectionFactory,
                                                    @NotNull final Predicate<? super T> predicate) {
        return filterTo(collectionFactory, predicate.negate());
    }

    default <C extends Collection<T>> C filterIndexedTo(@NotNull final Supplier<C> collectionFactory,
                                                        @NotNull final IndexedPredicate<? super T> predicate) {
        final var collection = collectionFactory.get();
        final Iterable<IndexedValue<T>> indexedIterable = this::indexedIterator;
        for (final var indexedValue : indexedIterable) {
            final var value = indexedValue.value();
            if (predicate.test(indexedValue.index(), value)) {
                collection.add(value);
            }
        }
        return collection;
    }

    default <C extends Collection<T>> C skipTo(final Supplier<C> collectionFactory, final int count) {
        final var collection = collectionFactory.get();
        var counter = 0;
        for (final var value : this) {
            if (counter >= count) {
                collection.add(value);
            }
            counter++;
        }
        return collection;
    }

    default  <C extends Collection<T>> C skipWhileTo(
            @NotNull final Supplier<C> collectionFactory,
            @NotNull final Predicate<? super T> predicate,
            final boolean inclusive) {
        var yielding = false;
        final var list = collectionFactory.get();
        for (final var item : this) {
            if (yielding) {
                list.add(item);
                continue;
            }
            if (!predicate.test(item)) {
                if (!inclusive) {
                    list.add(item);
                }
                yielding = true;
            }
        }
        return list;
    }

    default <C extends Collection<T>> C takeTo(final Supplier<C> collectionFactory, final int n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        final var collection = collectionFactory.get();
        if (n == 0) {
            return collection;
        }
        var count = 0;
        for (final var t : this) {
            collection.add(t);
            if (++count == n) {
                break;
            }
        }
        return collection;
    }

    default <C extends Collection<T>> C takeWhileTo(@NotNull final Supplier<C> collectionFactory,
                                                    @NotNull final Predicate<? super T> predicate,
                                                    final boolean inclusive) {
        final var iterator = iterator();
        final var collection = collectionFactory.get();
        if (!iterator.hasNext()) {
            return collection;
        }
        while (iterator.hasNext()) {
            final var item = iterator.next();
            if (!predicate.test(item)) {
                if (inclusive) {
                    collection.add(item);
                }
                break;
            }
            collection.add(item);
        }
        return collection;
    }

    default <R, C extends Collection<T>> C distinctTo(@NotNull final Supplier<C> collectionFactory,
                                                      @NotNull final Function<? super T, ? extends R> selector) {
        final var c = collectionFactory.get();
        final MutableSetX<R> set = MutableLinkedSetX.empty();
        for (final var t : this) {
            if (t != null) {
                final var r = selector.apply(t);
                if (set.add(r)) {
                    c.add(t);
                }
            }
        }
        return c;
    }

    default <A, R, C extends Collection<R>> C zipTo(
            @NotNull final Supplier<C> collectionFactory,
            @NotNull final Iterable<A> otherIterable,
            @NotNull final BiFunction<? super T, ? super A, ? extends R> function) {
        final var otherIterator = otherIterable.iterator();
        final var iterator = iterator();
        final var collection = collectionFactory.get();
        while (iterator.hasNext() && otherIterator.hasNext()) {
            final var next = iterator.next();
            final var otherNext = otherIterator.next();
            collection.add(function.apply(next, otherNext));
        }
        return collection;
    }

    default <R, C extends Collection<R>> C zipWithNextTo(@NotNull final Supplier<C> collectionFactory,
                                                         @NotNull final BiFunction<? super T, ? super T, ? extends R> function) {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            return collectionFactory.get();
        }
        final var collection = collectionFactory.get();
        var current = iterator.next();
        while (iterator.hasNext()) {
            final var next = iterator.next();
            collection.add(function.apply(current, next));
            current = next;
        }
        return collection;
    }
}
