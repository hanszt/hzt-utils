package org.hzt.utils.collections;

import org.hzt.utils.It;
import org.hzt.utils.PreConditions;
import org.hzt.utils.Sizable;
import org.hzt.utils.collections.primitives.DoubleList;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.LongList;
import org.hzt.utils.function.IndexedBiFunction;
import org.hzt.utils.function.IndexedFunction;
import org.hzt.utils.function.IndexedPredicate;
import org.hzt.utils.iterables.IterableExtension;
import org.hzt.utils.iterables.IterableX;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.tuples.IndexedValue;

import java.util.*;
import java.util.function.*;
import java.util.stream.Gatherer;

/// An immutable Collection interface. There are no mutating methods defined in this interface or parents.
public interface CollectionX<E> extends IterableX<E>, Sizable {

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    default <C extends Collection<E>> C ifEmpty(final Supplier<C> collectionFactory) {
        return isEmpty() ? collectionFactory.get() : to(collectionFactory);
    }

    default boolean isNotEmpty() {
        return !isEmpty();
    }

    default boolean contains(final E value) {
        return any(item -> item.equals(value));
    }

    default boolean containsNot(final E e) {
        return !contains(e);
    }

    default boolean containsAll(final Iterable<E> iterable) {
        return Sequence.of(iterable).all(this::contains);
    }

    default ListX<E> plus(final E value) {
        return ListX.build(size() + 1, ml -> {
            ml.addAll(this);
            ml.add(value);
        });
    }

    default ListX<E> plus(final Iterable<? extends E> values) {
        final var otherSize = switch (values) {
            case Collection<?> c -> c.size();
            case CollectionX<?> c -> c.size();
            default -> 0;
        };
        return ListX.build(size() + otherSize, ml -> {
            ml.addAll(this);
            ml.addAll(values);
        });
    }

    @Override
    default ListX<E> minus(final E value) {
        return asSequence().minus(value).toListX();
    }

    @Override
    default ListX<E> minus(final Iterable<? extends E> values) {
        return asSequence().minus(values).toListX();
    }

    @Override
    default ListX<E> merge(Iterable<? extends E> other) {
        final var it1 = iterator();
        final var it2 = other.iterator();
        final var size = size(other);
        return ListX.build(size() + size, ml -> {
            while (it1.hasNext() || it2.hasNext()) {
                if (it1.hasNext()) {
                    ml.add(it1.next());
                }
                if (it2.hasNext()) {
                    ml.add(it2.next());
                }
            }
        });
    }

    default boolean containsNoneOf(final Iterable<E> iterable) {
        return Sequence.of(iterable).none(this::contains);
    }

    default <R> ListX<R> map(final Function<? super E, ? extends R> mapper) {
        return ListX.build(size(), ml -> mapTo(() -> ml, mapper));
    }

    default <R> ListX<R> mapIndexed(final IndexedFunction<? super E, ? extends R> mapper) {
        return ListX.build(size(), ml -> mapIndexedTo(() -> ml, mapper));
    }

    default ListX<E> filter(final Predicate<? super E> predicate) {
        return ListX.build(size(), ml -> filterTo(() -> ml, predicate));
    }

    default <R> ListX<E> filterBy(
            final Function<? super E, ? extends R> selector,
            final Predicate<? super R> predicate
    ) {
        Objects.requireNonNull(selector);
        Objects.requireNonNull(predicate);
        return asSequence()
                .filter(Objects::nonNull)
                .filter(t -> predicate.test(selector.apply(t)))
                .toListX();
    }

    default ListX<E> filterIndexed(final IndexedPredicate<? super E> predicate) {
        return ListX.build(size(), ml -> filterIndexedTo(() -> ml, predicate));
    }

    default ListX<E> filterNot(final Predicate<? super E> predicate) {
        return ListX.build(size(), ml -> IterableX.super.filterNotTo(() -> ml, predicate));
    }

    @Override
    default IntList mapToInt(final ToIntFunction<? super E> mapper) {
        return IntList.build(size(), ml -> {
            for (final var e : this) {
                ml.add(mapper.applyAsInt(e));
            }
        });
    }

    @Override
    default LongList mapToLong(final ToLongFunction<? super E> mapper) {
        return LongList.build(size(), ml -> {
            for (final var e : this) {
                ml.add(mapper.applyAsLong(e));
            }
        });
    }

    @Override
    default DoubleList mapToDouble(final ToDoubleFunction<? super E> mapper) {
        return DoubleList.build(size(), ml -> {
            for (final var e : this) {
                ml.add(mapper.applyAsDouble(e));
            }
        });
    }

    default <R> ListX<R> flatMap(final Function<? super E, ? extends Iterable<? extends R>> mapper) {
        return ListX.build(size(), ml -> flatMapTo(() -> ml, mapper));
    }

    @Override
    default IntList flatMapToInt(final Function<? super E, ? extends PrimitiveIterable.OfInt> mapper) {
        return IntList.build(size(), ml -> flatMapIntsTo(() -> ml, mapper));
    }

    @Override
    default LongList flatMapToLong(final Function<? super E, ? extends PrimitiveIterable.OfLong> mapper) {
        return LongList.build(size(), ml -> flatMapLongsTo(() -> ml, mapper));
    }

    @Override
    default DoubleList flatMapToDouble(final Function<? super E, ? extends PrimitiveIterable.OfDouble> mapper) {
        return DoubleList.build(size(), ml -> flatMapDoublesTo(() -> ml, mapper));
    }

    default <R> ListX<R> mapMulti(final BiConsumer<? super E, ? super Consumer<R>> mapper) {
        return ListX.build(size(), ml -> mapMultiTo(() -> ml, mapper));
    }

    @Override
    default <R> ListX<R> gather(final Gatherer<? super E, ?, R> gatherer) {
        return ListX.build(ml -> gatherTo(() -> ml, gatherer));
    }

    default <R> ListX<R> then(final IterableExtension<E, R> extension) {
        return ListX.of(() -> extension.extend(this).iterator());
    }

    @Override
    default IntList mapMultiToInt(final BiConsumer<? super E, IntConsumer> mapper) {
        return (IntList) IterableX.super.mapMultiToInt(mapper);
    }

    @Override
    default LongList mapMultiToLong(final BiConsumer<? super E, LongConsumer> mapper) {
        return (LongList) IterableX.super.mapMultiToLong(mapper);
    }

    @Override
    default DoubleList mapMultiToDouble(final BiConsumer<? super E, DoubleConsumer> mapper) {
        return (DoubleList) IterableX.super.mapMultiToDouble(mapper);
    }

    default <R> ListX<R> mapNotNull(final Function<? super E, ? extends R> mapper) {
        return ListX.build(size(), ml -> mapNotNullTo(() -> ml, mapper));
    }

    default <R> ListX<R> mapIfPresent(final Function<? super E, Optional<R>> mapper) {
        return ListX.build(size(), ml -> mapIfPresentTo(() -> ml, mapper));
    }

    @Override
    default <R> ListX<R> filterIsInstance(final Class<R> aClass) {
        return asSequence().filter(aClass::isInstance).map(aClass::cast).toListX();
    }

    default ListX<IndexedValue<E>> withIndex() {
        return Sequence.of(this::indexedIterator).toListX();
    }

    @Override
    default ListX<E> sorted() {
        return (ListX<E>) IterableX.super.sorted();
    }

    @Override
    default <R extends Comparable<? super R>> ListX<E> sortedBy(final Function<? super E, ? extends R> selector) {
        return (ListX<E>) IterableX.super.sortedBy(selector);
    }

    @Override
    default ListX<E> sorted(final Comparator<? super E> comparator) {
        return (ListX<E>) IterableX.super.sorted(comparator);
    }

    @Override
    default ListX<E> sortedDescending() {
        return (ListX<E>) IterableX.super.sortedDescending();
    }

    @Override
    default <R extends Comparable<? super R>> ListX<E> sortedByDescending(final Function<? super E, ? extends R> selector) {
        return (ListX<E>) IterableX.super.sortedByDescending(selector);
    }

    @Override
    default ListX<E> distinct() {
        return distinctBy(It::self);
    }

    @Override
    default <R> ListX<E> distinctBy(final Function<? super E, ? extends R> selector) {
        return ListX.build(ml -> distinctTo(() -> ml, selector));
    }

    default ListX<ListX<E>> chunked(final int size) {
        return windowed(size, size, true);
    }

    default ListX<ListX<E>> windowed(final int size) {
        return windowed(size, 1);
    }

    default <R> ListX<R> windowed(final int size, final Function<? super ListX<E>, ? extends R> transform) {
        return windowed(size, 1).map(transform);
    }

    default ListX<ListX<E>> windowed(final int size, final int step) {
        return windowed(size, step, false);
    }

    default <R> ListX<R> windowed(final int size, final int step, final Function<? super ListX<E>, ? extends R> transform) {
        return windowed(size, step, false).map(transform);
    }

    default ListX<ListX<E>> windowed(final int size, final boolean partialWindows) {
        return windowed(size, 1, partialWindows);
    }

    default ListX<ListX<E>> windowed(final int size, final int step, final boolean partialWindows) {
        return windowed(size, step, partialWindows, It::self);
    }

    default <R> ListX<R> windowed(final int size, final int step, final boolean partialWindows,
                                  final Function<? super ListX<E>, R> transform) {
        return asSequence().windowed(size, step, partialWindows).map(transform).toListX();
    }

    default <A, R> ListX<R> zip(final Iterable<A> iterable, final BiFunction<? super E, ? super A, ? extends R> function) {
        final var otherSize = switch (iterable) {
            case Collection<A> c -> c.size();
            case CollectionX<A> c -> c.size();
            default -> 0;
        };
        return ListX.build(size() + otherSize, ml -> zipTo(() -> ml, iterable, function));
    }

    default <R> ListX<R> zipWithNext(final BiFunction<? super E, ? super E, ? extends R> function) {
        return ListX.build(size() - 1, ml -> zipWithNextTo(() -> ml, function));
    }

    default <K> MapX<K, E> associateBy(final Function<? super E, ? extends K> keyMapper) {
        return toMapX(keyMapper, It::self);
    }

    default <V> MapX<E, V> associateWith(final Function<? super E, ? extends V> valueMapper) {
        return toMapX(It::self, valueMapper);
    }

    @Override
    default <R> ListX<R> scan(final R initial, final BiFunction<? super R, ? super E, ? extends R> operation) {
        Objects.requireNonNull(operation);
        return ListX.build(size() + 1, ml -> {
            ml.add(initial);
            var accumulation = initial;
            for (final var value : this) {
                accumulation = operation.apply(accumulation, value);
                ml.add(accumulation);
            }
        });
    }

    @Override
    default <R> ListX<R> scanIndexed(final R initial, final IndexedBiFunction<? super R, ? super E, ? extends R> operation) {
        Objects.requireNonNull(operation);
        return ListX.build(size() + 1, ml -> {
            ml.add(initial);
            var index = 0;
            var accumulation = initial;
            for (final var value : this) {
                accumulation = operation.apply(index, accumulation, value);
                ml.add(accumulation);
                index++;
            }
        });
    }

    default ListX<E> skip(final long count) {
        final var capacity = size() - (int) count;
        return ListX.build(capacity, ml -> skipTo(() -> ml, (int) count));
    }

    @Override
    default ListX<E> skipWhile(final Predicate<? super E> predicate) {
        return ListX.build(ml -> skipWhileTo(() -> ml, predicate, false));
    }

    @Override
    default ListX<E> skipWhileInclusive(final Predicate<? super E> predicate) {
        return ListX.build(ml -> skipWhileTo(() -> ml, predicate, true));
    }

    @Override
    default ListX<E> take(final long n) {
        PreConditions.require(n <= Integer.MAX_VALUE);
        return ListX.build((int) n, ml -> takeTo(() -> ml, (int) n));
    }

    default ListX<E> takeWhile(final Predicate<? super E> predicate) {
        return ListX.build(ml -> takeWhileTo(() -> ml, predicate, false));
    }

    default ListX<E> takeWhileInclusive(final Predicate<? super E> predicate) {
        return ListX.build(ml -> takeWhileTo(() -> ml, predicate, true));
    }

    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(iterator(), size(), Spliterator.SIZED);
    }

    @Override
    default MutableListX<E> toMutableList() {
        return to(() -> MutableListX.withInitCapacity(size()));
    }

    private static <E> int size(final Iterable<? extends E> other) {
        return switch (other) {
            case Collection<?> c -> c.size();
            case CollectionX<?> c -> c.size();
            default -> 0;
        };
    }
}
