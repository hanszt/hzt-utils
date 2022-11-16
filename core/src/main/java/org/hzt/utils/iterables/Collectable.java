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
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.tuples.IndexedValue;
import org.hzt.utils.tuples.Pair;
import org.hzt.utils.tuples.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    default T[] toTypedArray(IntFunction<T[]> generator) {
        return toArrayOf(It::self, generator);
    }

    default <R> R[] toArrayOf(@NotNull Function<? super T, ? extends R> mapper, @NotNull IntFunction<R[]> generator) {
        MutableListX<R> list = MutableListX.empty();
        for (T t : this) {
            if (t != null) {
                list.add(mapper.apply(t));
            }
        }
        return list.toArray(generator.apply(0));
    }

    default <K, V> MutableMapX<K, V> toMutableMap(@NotNull Function<? super T, ? extends K> keyMapper,
                                                  @NotNull Function<? super T, ? extends V> valueMapper) {
        MutableMapX<K, V> map = MutableMapX.empty();
        for (T t : this) {
            if (t != null) {
                final K key = keyMapper.apply(t);
                if (key != null) {
                    map.put(key, valueMapper.apply(t));
                }
            }
        }
        return map;
    }

    default <K, V> MapX<K, V> toMapX(@NotNull Function<? super T, ? extends K> keyMapper,
                                     @NotNull Function<? super T, ? extends V> valueMapper) {
        return toMutableMap(keyMapper, valueMapper);
    }

    default <K, V> Map<K, V> toMap(@NotNull Function<? super T, ? extends K> keyMapper,
                                   @NotNull Function<? super T, ? extends V> valueMapper) {
        return Collections.unmodifiableMap(toMutableMap(keyMapper, valueMapper));
    }

    default <R, A> R collect(@NotNull Collector<? super T, A, R> collector) {
        final A result = collector.supplier().get();
        final BiConsumer<A, ? super T> accumulator = collector.accumulator();
        for (T item : this) {
            if (item != null) {
                accumulator.accept(result, item);
            }
        }
        return collector.finisher().apply(result);
    }

    default <A, R> R collect(@NotNull Supplier<A> supplier,
                             @NotNull BiConsumer<A, ? super T> accumulator,
                             @NotNull Function<A, R> finisher) {
        return collect(supplier, It::noFilter, It::self, It::noFilter, accumulator, finisher);
    }

    default <A, R> A collect(@NotNull Supplier<A> supplier,
                             @NotNull Predicate<T> filter,
                             @NotNull Function<T, R> mapper,
                             @NotNull BiConsumer<A, ? super R> accumulator) {
        return collect(supplier, filter, mapper, It::noFilter, accumulator, It::self);
    }

    default <A, U, R> R collect(@NotNull Supplier<A> supplier,
                                @NotNull Predicate<T> filter,
                                @NotNull Function<T, U> mapper,
                                @NotNull Predicate<U> resultFilter,
                                @NotNull BiConsumer<A, ? super U> accumulator,
                                @NotNull Function<A, R> finisher) {
        A result = supplier.get();
        for (T item : this) {
            if (filter.test(item)) {
                U u = mapper.apply(item);
                if (resultFilter.test(u)) {
                    accumulator.accept(result, u);
                }
            }
        }
        return finisher.apply(result);
    }

    default <R1, A1, R2, A2, R> R teeing(@NotNull Collector<? super T, A1, R1> downstream1,
                                 @NotNull Collector<? super T, A2, R2> downstream2,
                                 @NotNull BiFunction<? super R1, ? super R2, R> merger) {
        A1 result1 = downstream1.supplier().get();
        A2 result2 = downstream2.supplier().get();
        final BiConsumer<A1, ? super T> accumulator1 = downstream1.accumulator();
        final BiConsumer<A2, ? super T> accumulator2 = downstream2.accumulator();
        for (T item : this) {
            if (item != null) {
                accumulator1.accept(result1, item);
                accumulator2.accept(result2, item);
            }
        }
        final R1 r1 = downstream1.finisher().apply(result1);
        final R2 r2 = downstream2.finisher().apply(result2);
        return merger.apply(r1, r2);
    }

    default <R1, R2> Pair<R1, R2> teeing(@NotNull Collector<? super T, ?, R1> downstream1,
                                         @NotNull Collector<? super T, ?, R2> downstream2) {
        return teeing(downstream1, downstream2, Pair::of);
    }

    default <R1, A1, R2, A2, R3, A3, R> R branching(@NotNull Collector<? super T, A1, R1> downstream1,
                                        @NotNull Collector<? super T, A2, R2> downstream2,
                                        @NotNull Collector<? super T, A3, R3> downstream3,
                                        @NotNull TriFunction<? super R1, ? super R2, ? super R3, R> merger) {
        A1 result1 = downstream1.supplier().get();
        A2 result2 = downstream2.supplier().get();
        A3 result3 = downstream3.supplier().get();
        final BiConsumer<A1, ? super T> accumulator1 = downstream1.accumulator();
        final BiConsumer<A2, ? super T> accumulator2 = downstream2.accumulator();
        final BiConsumer<A3, ? super T> accumulator3 = downstream3.accumulator();
        for (T item : this) {
            if (item != null) {
                accumulator1.accept(result1, item);
                accumulator2.accept(result2, item);
                accumulator3.accept(result3, item);
            }
        }
        final R1 r1 = downstream1.finisher().apply(result1);
        final R2 r2 = downstream2.finisher().apply(result2);
        final R3 r3 = downstream3.finisher().apply(result3);
        return merger.apply(r1, r2, r3);
    }

    default <R1, R2, R3> Triple<R1, R2, R3> branching(@NotNull Collector<? super T, ?, R1> downstream1,
                                                      @NotNull Collector<? super T, ?, R2> downstream2,
                                                      @NotNull Collector<? super T, ?, R3> downstream3) {
        return branching(downstream1, downstream2, downstream3, Triple::of);
    }

    default <A1, R1, A2, R2, A3, R3, A4, R4, R> R branching(@NotNull Collector<? super T, A1, R1> downstream1,
                                        @NotNull Collector<? super T, A2, R2> downstream2,
                                        @NotNull Collector<? super T, A3, R3> downstream3,
                                        @NotNull Collector<? super T, A4, R4> downstream4,
                                        @NotNull QuadFunction<? super R1, ? super R2, ? super R3, ? super R4, R> merger) {
        A1 result1 = downstream1.supplier().get();
        A2 result2 = downstream2.supplier().get();
        A3 result3 = downstream3.supplier().get();
        A4 result4 = downstream4.supplier().get();
        final BiConsumer<A1, ? super T> accumulator1 = downstream1.accumulator();
        final BiConsumer<A2, ? super T> accumulator2 = downstream2.accumulator();
        final BiConsumer<A3, ? super T> accumulator3 = downstream3.accumulator();
        final BiConsumer<A4, ? super T> accumulator4 = downstream4.accumulator();
        for (T item : this) {
            if (item != null) {
                accumulator1.accept(result1, item);
                accumulator2.accept(result2, item);
                accumulator3.accept(result3, item);
                accumulator4.accept(result4, item);
            }
        }
        final R1 r1 = downstream1.finisher().apply(result1);
        final R2 r2 = downstream2.finisher().apply(result2);
        final R3 r3 = downstream3.finisher().apply(result3);
        final R4 r4 = downstream4.finisher().apply(result4);
        return merger.apply(r1, r2, r3, r4);
    }

    default <C extends Collection<T>> C to(Supplier<C> collectionFactory) {
        return IterableXHelper.mapFilteringTo(this, collectionFactory, It::noFilter, It::self, It::noFilter);
    }

    default MutableListX<T> toMutableList() {
        return to(MutableListX::empty);
    }

    default ListX<T> toListX() {
        return ListX.copyOf(toMutableList());
    }

    default List<T> toList() {
        return Collections.unmodifiableList(toMutableList());
    }

    default <R> List<R> toListOf(@NotNull Function<? super T, ? extends R> transform) {
        final List<? extends R> list = mapNotNullTo(MutableListX::empty, transform);
        return Collections.unmodifiableList(list);
    }

    default MutableSetX<T> toMutableSet() {
        return IterableXHelper.mapFilteringTo(this, MutableSetX::empty, Objects::nonNull, It::self, It::noFilter);
    }

    default SetX<T> toSetX() {
        return SetX.copyOf(toMutableSet());
    }

    default <R> SetX<R> toSetXOf(@NotNull Function<? super T, ? extends R> transform) {
        return mapNotNullTo(MutableSetX::empty, transform);
    }

    default Set<T> toSet() {
        return Collections.unmodifiableSet(toMutableSet());
    }

    default <R> Set<R> toSetOf(@NotNull Function<? super T, ? extends R> transform) {
        return Collections.unmodifiableSet(this.<R, MutableSetX<R>>mapNotNullTo(MutableSetX::empty, transform));
    }

    default <R, C extends Collection<R>> C mapTo(@NotNull Supplier<C> collectionFactory,
                                                 @NotNull Function<? super T, ? extends R> mapper) {
        return IterableXHelper.mapFilteringTo(this, collectionFactory, Objects::nonNull, mapper, It::noFilter);
    }
    default <R, C extends Collection<R>> C mapNotNullTo(@NotNull Supplier<C> collectionFactory,
                                                        @NotNull Function<? super T, ? extends R> mapper) {
        return IterableXHelper.mapFilteringTo(this, collectionFactory, Objects::nonNull, mapper, Objects::nonNull);
    }

    default <R, C extends Collection<R>> C mapIndexedTo(@NotNull Supplier<C> collectionFactory,
                                                        @NotNull IndexedFunction<? super T, ? extends R> mapper) {
        return Sequence.of(this::indexedIterator)
                .mapTo(collectionFactory, indexedValue -> mapper.apply(indexedValue.index(), indexedValue.value()));
    }

    default <R, I extends Iterable<? extends R>, C extends Collection<R>> C flatMapTo(
            @NotNull Supplier<C> collectionSupplier,
            @NotNull Function<? super T, ? extends I> mapper) {
        final C collection = collectionSupplier.get();
        for (T item : this) {
            final I c = mapper.apply(item);
            if (c == null) {
                continue;
            }
            for (R r : c) {
                if (r != null) {
                    collection.add(r);
                }
            }
        }
        return collection;
    }

    default <I extends PrimitiveIterable.OfInt, C extends IntMutableCollection> C flatMapIntsTo(
            @NotNull Supplier<C> collectionSupplier,
            @NotNull Function<? super T, ? extends I> mapper) {
        final C collection = collectionSupplier.get();
        for (T item : this) {
            final I c = mapper.apply(item);
            if (c == null) {
                continue;
            }
            for (int value : c) {
                collection.add(value);
            }
        }
        return collection;
    }

    default <I extends PrimitiveIterable.OfLong, C extends LongMutableCollection> C flatMapLongsTo(
            @NotNull Supplier<C> collectionSupplier,
            @NotNull Function<? super T, ? extends I> mapper) {
        final C collection = collectionSupplier.get();
        for (T item : this) {
            final I c = mapper.apply(item);
            if (c == null) {
                continue;
            }
            for (long value : c) {
                collection.add(value);
            }
        }
        return collection;
    }

    default <I extends PrimitiveIterable.OfDouble, C extends DoubleMutableCollection> C flatMapDoublesTo(
            @NotNull Supplier<C> collectionSupplier,
            @NotNull Function<? super T, ? extends I> mapper) {
        final C collection = collectionSupplier.get();
        for (T item : this) {
            final I c = mapper.apply(item);
            if (c == null) {
                continue;
            }
            for (double value : c) {
                collection.add(value);
            }
        }
        return collection;
    }

    default <R, C extends Collection<R>> C mapMultiTo(
            @NotNull Supplier<C> collectionSupplier,
            @NotNull BiConsumer<? super T, ? super Consumer<R>> mapper) {
        C collection = collectionSupplier.get();
        for (T item : this) {
            mapper.accept(item, (Consumer<R>) collection::add);
        }
        return collection;
    }

    default <C extends Collection<T>> C filterTo(@NotNull Supplier<C> collectionFactory,
                                                 @NotNull Predicate<? super T> predicate) {
        return IterableXHelper.mapFilteringTo(this, collectionFactory, predicate, It::self, It::noFilter);
    }

    default <C extends Collection<T>> C filterNotTo(@NotNull Supplier<C> collectionFactory,
                                                    @NotNull Predicate<? super T> predicate) {
        return filterTo(collectionFactory, predicate.negate());
    }

    default <C extends Collection<T>> C filterIndexedTo(@NotNull Supplier<C> collectionFactory,
                                                        @NotNull IndexedPredicate<? super T> predicate) {
        C collection = collectionFactory.get();
        final Iterable<IndexedValue<T>> indexedIterable = this::indexedIterator;
        for (IndexedValue<T> indexedValue : indexedIterable) {
            final T value = indexedValue.value();
            if (predicate.test(indexedValue.index(), value)) {
                collection.add(value);
            }
        }
        return collection;
    }

    default <C extends Collection<T>> C skipTo(Supplier<C> collectionFactory, int count) {
        C collection = collectionFactory.get();
        int counter = 0;
        for (T value : this) {
            if (counter >= count) {
                collection.add(value);
            }
            counter++;
        }
        return collection;
    }

    default  <C extends Collection<T>> C skipWhileTo(
            @NotNull Supplier<C> collectionFactory,
            @NotNull Predicate<? super T> predicate,
            boolean inclusive) {
        boolean yielding = false;
        C list = collectionFactory.get();
        for (T item : this) {
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

    default <C extends Collection<T>> C takeTo(Supplier<C> collectionFactory, long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        C collection = collectionFactory.get();
        if (n == 0) {
            return collection;
        }
        final Iterable<T> iterable = this;
        if (iterable instanceof Collection) {
            Collection<T> c = (Collection<T>) iterable;
            if (n >= c.size()) {
                collection.addAll(c);
                return collection;
            }
        }
        int count = 0;
        for (T t : this) {
            collection.add(t);
            if (++count == n) {
                break;
            }
        }
        return collection;
    }

    default <C extends Collection<T>> C takeWhileTo(@NotNull Supplier<C> collectionFactory,
                                                    @NotNull Predicate<? super T> predicate,
                                                    boolean inclusive) {
        final C collection = collectionFactory.get();
        for (T item : this) {
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

    default <R, C extends Collection<T>> C distinctTo(@NotNull Supplier<C> collectionFactory,
                                                      @NotNull Function<? super T, ? extends R> selector) {
        C c = collectionFactory.get();
        MutableSetX<R> set = MutableLinkedSetX.empty();
        for (T t : this) {
            if (t != null) {
                final R r = selector.apply(t);
                if (set.add(r)) {
                    c.add(t);
                }
            }
        }
        return c;
    }

    default <A, R, C extends Collection<R>> C zipTo(
            @NotNull Supplier<C> collectionFactory,
            @NotNull Iterable<A> otherIterable,
            @NotNull BiFunction<? super T, ? super A, ? extends R> function) {
        final Iterator<A> otherIterator = otherIterable.iterator();
        final Iterator<T> iterator = iterator();
        final C list = collectionFactory.get();
        while (iterator.hasNext() && otherIterator.hasNext()) {
            final T next = iterator.next();
            final A otherNext = otherIterator.next();
            list.add(function.apply(next, otherNext));
        }
        return list;
    }

    default <R, C extends Collection<R>> C zipWithNextTo(@NotNull Supplier<C> collectionFactory,
                                                         @NotNull BiFunction<? super T, ? super T, ? extends R> function) {
        final Iterator<T> iterator = iterator();
        if (!iterator.hasNext()) {
            return collectionFactory.get();
        }
        final C list = collectionFactory.get();
        T current = iterator.next();
        while (iterator.hasNext()) {
            final T next = iterator.next();
            list.add(function.apply(current, next));
            current = next;
        }
        return list;
    }
}
