package org.hzt.utils.collections;

import org.hzt.utils.It;
import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.primitives.DoubleList;
import org.hzt.utils.collections.primitives.DoubleMutableList;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.IntMutableList;
import org.hzt.utils.collections.primitives.LongList;
import org.hzt.utils.collections.primitives.LongMutableList;
import org.hzt.utils.function.IndexedFunction;
import org.hzt.utils.function.IndexedPredicate;
import org.hzt.utils.iterables.IterableX;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.tuples.IndexedValue;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

@FunctionalInterface
@SuppressWarnings("squid:S1448")
public interface CollectionX<E> extends IterableX<E> {

    default int size() {
        return (int) count(It::noFilter);
    }

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

    default boolean containsAll(@NotNull final Iterable<E> iterable) {
        return Sequence.of(iterable).all(this::contains);
    }

    default ListX<E> plus(@NotNull final E value) {
        final var list = MutableListX.of(this);
        list.add(value);
        return ListX.copyOf(list);
    }

    default ListX<E> plus(@NotNull final Iterable<? extends E> values) {
        return ListX.copyOfNullsAllowed(MutableListX.of(this).plus(values));
    }

    @Override
    default ListX<E> minus(@NotNull final E value) {
        return asSequence().minus(value).toListX();
    }

    @Override
    default ListX<E> minus(@NotNull final Iterable<E> values) {
        return asSequence().minus(values).toListX();
    }

    default boolean containsNoneOf(@NotNull final Iterable<E> iterable) {
        return Sequence.of(iterable).none(this::contains);
    }

    default <R> ListX<R> map(@NotNull final Function<? super E, ? extends R> mapper) {
        final MutableListX<R> listX = mapTo(() -> MutableListX.withInitCapacity(size()), mapper);
        return ListX.copyOfNullsAllowed(listX);
    }

    default <R> ListX<R> mapIndexed(@NotNull final IndexedFunction<? super E, ? extends R> mapper) {
        return ListX.copyOf(mapIndexedTo(() -> MutableListX.withInitCapacity(size()), mapper));
    }

    default ListX<E> filter(@NotNull final Predicate<? super E> predicate) {
        return ListX.copyOf(filterTo(() -> MutableListX.withInitCapacity(size()), predicate));
    }

    default <R> ListX<E> filterBy(@NotNull final Function<? super E, ? extends R> selector,
                                  @NotNull final Predicate<? super R> predicate) {
        return filter(Objects::nonNull).filter(t -> predicate.test(selector.apply(t)));
    }

    default ListX<E> filterIndexed(@NotNull final IndexedPredicate<? super E> predicate) {
        return ListX.copyOf(filterIndexedTo(() -> MutableListX.withInitCapacity(size()), predicate));
    }

    default ListX<E> filterNot(@NotNull final Predicate<? super E> predicate) {
        return ListX.copyOf(IterableX.super.filterNotTo(() -> MutableListX.withInitCapacity(size()), predicate));
    }

    @Override
    default IntList mapToInt(@NotNull final ToIntFunction<? super E> mapper) {
        final var intList = IntMutableList.withInitCapacity(size());
        for (final var e : this) {
            intList.add(mapper.applyAsInt(e));
        }
        return IntList.copyOf(intList);
    }

    @Override
    default LongList mapToLong(@NotNull final ToLongFunction<? super E> mapper) {
        final var longList = LongMutableList.withInitCapacity(size());
        for (final var e : this) {
            longList.add(mapper.applyAsLong(e));
        }
        return LongList.copyOf(longList);
    }

    @Override
    default DoubleList mapToDouble(@NotNull final ToDoubleFunction<? super E> mapper) {
        final var doubleList = DoubleMutableList.withInitCapacity(size());
        for (final var e : this) {
            doubleList.add(mapper.applyAsDouble(e));
        }
        return DoubleList.copyOf(doubleList);
    }

    default <R> ListX<R> flatMap(@NotNull final Function<? super E, ? extends Iterable<? extends R>> mapper) {
        return ListX.copyOf(flatMapTo(() -> MutableListX.withInitCapacity(size()), mapper));
    }

    @Override
    default IntList flatMapToInt(@NotNull final Function<? super E, ? extends PrimitiveIterable.OfInt> mapper) {
        return IntList.copyOf(flatMapIntsTo(() -> IntMutableList.withInitCapacity(size()), mapper));
    }

    @Override
    default LongList flatMapToLong(@NotNull final Function<? super E, ? extends PrimitiveIterable.OfLong> mapper) {
        return LongList.copyOf(flatMapLongsTo(() -> LongMutableList.withInitCapacity(size()), mapper));
    }

    @Override
    default DoubleList flatMapToDouble(@NotNull final Function<? super E, ? extends PrimitiveIterable.OfDouble> mapper) {
        return DoubleList.copyOf(flatMapDoublesTo(() -> DoubleMutableList.withInitCapacity(size()), mapper));
    }

    default <R> ListX<R> mapMulti(@NotNull final BiConsumer<? super E, ? super Consumer<R>> mapper) {
        return ListX.copyOf(mapMultiTo(() -> MutableListX.withInitCapacity(size()), mapper));
    }

    @Override
    default IntList mapMultiToInt(@NotNull final BiConsumer<? super E, IntConsumer> mapper) {
        return (IntList) IterableX.super.mapMultiToInt(mapper);
    }

    @Override
    default LongList mapMultiToLong(@NotNull final BiConsumer<? super E, LongConsumer> mapper) {
        return (LongList) IterableX.super.mapMultiToLong(mapper);
    }

    @Override
    default DoubleList mapMultiToDouble(@NotNull final BiConsumer<? super E, DoubleConsumer> mapper) {
        return (DoubleList) IterableX.super.mapMultiToDouble(mapper);
    }

    default <R> ListX<R> mapNotNull(@NotNull final Function<? super E, ? extends R> mapper) {
        return ListX.copyOf(mapNotNullTo(() -> MutableListX.withInitCapacity(size()), mapper));
    }

    default <R> ListX<R> mapIfPresent(@NotNull final Function<? super E, Optional<R>> mapper) {
        return ListX.copyOf(mapIfPresentTo(() -> MutableListX.withInitCapacity(size()), mapper));
    }

    @Override
    default <R> ListX<R> castIfInstanceOf(@NotNull final Class<R> aClass) {
        return asSequence().filter(aClass::isInstance).map(aClass::cast).toListX();
    }

    default ListX<IndexedValue<E>> withIndex() {
        return Sequence.of(this::indexedIterator).toListX();
    }

    @Override
    default ListX<E> sorted() {
        final var sorted = (MutableListX<E>) IterableX.super.sorted();
        return ListX.copyOf(sorted);
    }

    @Override
    default <R extends Comparable<? super R>> ListX<E> sortedBy(@NotNull final Function<? super E, ? extends R> selector) {
        final var sorted = (MutableListX<E>) IterableX.super.sortedBy(selector);
        return ListX.copyOf(sorted);
    }

    @Override
    default ListX<E> sorted(final Comparator<? super E> comparator) {
        final var sorted = (MutableListX<E>) IterableX.super.sorted(comparator);
        return ListX.copyOf(sorted);
    }

    @Override
    default ListX<E> sortedDescending() {
        final var listX = (MutableListX<E>) IterableX.super.sortedDescending();
        return ListX.copyOf(listX);
    }

    @Override
    default <R extends Comparable<? super R>> ListX<E> sortedByDescending(@NotNull final Function<? super E, ? extends R> selector) {
        final var listX = (MutableListX<E>) IterableX.super.sortedByDescending(selector);
        return ListX.copyOf(listX);
    }

    @Override
    default ListX<E> distinct() {
        return distinctBy(It::self);
    }

    @Override
    default <R> ListX<E> distinctBy(@NotNull final Function<? super E, ? extends R> selector) {
        return ListX.copyOf(distinctTo(MutableListX::empty, selector));
    }

    default ListX<ListX<E>> chunked(final int size) {
        return windowed(size, size, true);
    }

    default ListX<ListX<E>> windowed(final int size) {
        return windowed(size, 1);
    }

    default <R> ListX<R> windowed(final int size, @NotNull final Function<? super ListX<E>, ? extends R> transform) {
        return windowed(size, 1).map(transform);
    }

    default ListX<ListX<E>> windowed(final int size, final int step) {
        return windowed(size, step, false);
    }

    default <R> ListX<R> windowed(final int size, final int step, @NotNull final Function<? super ListX<E>, ? extends R> transform) {
        return windowed(size, step, false).map(transform);
    }

    default ListX<ListX<E>> windowed(final int size, final boolean partialWindows) {
        return windowed(size, 1, partialWindows);
    }

    default ListX<ListX<E>> windowed(final int size, final int step, final boolean partialWindows) {
        return windowed(size, step, partialWindows, It::self);
    }

    default <R> ListX<R> windowed(final int size, final int step, final boolean partialWindows,
                                  @NotNull final Function<? super ListX<E>, R> transform) {
        return asSequence().windowed(size, step, partialWindows).map(transform).toListX();
    }

    default <A, R> ListX<R> zip(@NotNull final Iterable<A> iterable, @NotNull final BiFunction<? super E, ? super A, ? extends R> function) {
        return ListX.copyOf(zipTo(MutableListX::empty, iterable, function));
    }

    default <R> ListX<R> zipWithNext(@NotNull final BiFunction<? super E, ? super E, ? extends R> function) {
        return ListX.copyOf(zipWithNextTo(MutableListX::empty, function));
    }

    default <K> MapX<K, E> associateBy(@NotNull final Function<? super E, ? extends K> keyMapper) {
        return toMapX(keyMapper, It::self);
    }

    default <V> MapX<E, V> associateWith(@NotNull final Function<? super E, ? extends V> valueMapper) {
        return toMapX(It::self, valueMapper);
    }

    @Override
    default <R> ListX<R> scan(final R initial, final BiFunction<? super R, ? super E, ? extends R> operation) {
        var accumulation = initial;
        final var mutableListX = MutableListX.of(initial);
        for (final var value : this) {
            accumulation = operation.apply(accumulation, value);
            mutableListX.add(accumulation);
        }
        return ListX.copyOf(mutableListX);
    }

    default ListX<E> skip(final long count) {
        return ListX.copyOf(skipTo(() -> MutableListX.withInitCapacity(size() - (int) count), (int) count));
    }

    @Override
    default ListX<E> skipWhile(@NotNull final Predicate<? super E> predicate) {
        return ListX.copyOf(skipWhileTo(MutableListX::empty, predicate, false));
    }

    @Override
    default ListX<E> skipWhileInclusive(@NotNull final Predicate<? super E> predicate) {
        return ListX.copyOf(skipWhileTo(MutableListX::empty, predicate, true));
    }

    @Override
    default ListX<E> take(final long n) {
        PreConditions.require(n <= Integer.MAX_VALUE);
        return ListX.copyOf(takeTo(() -> MutableListX.withInitCapacity((int) n), (int) n));
    }

    default ListX<E> takeWhile(@NotNull final Predicate<? super E> predicate) {
        return ListX.copyOf(takeWhileTo(MutableListX::empty, predicate, false));
    }

    default ListX<E> takeWhileInclusive(@NotNull final Predicate<? super E> predicate) {
        return ListX.copyOf(takeWhileTo(MutableListX::empty, predicate, true));
    }

    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(iterator(), size(), Spliterator.SIZED);
    }

    @Override
    default MutableListX<E> toMutableList() {
        return to(() -> MutableListX.withInitCapacity(size()));
    }
}
