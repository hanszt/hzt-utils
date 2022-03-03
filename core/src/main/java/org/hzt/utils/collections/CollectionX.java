package org.hzt.utils.collections;

import org.hzt.utils.iterables.IterableX;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.strings.StringX;
import org.hzt.utils.tuples.IndexedValue;
import org.hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@FunctionalInterface
public interface CollectionX<E> extends IterableX<E> {

    default int size() {
        return (int) count(It::noFilter);
    }

    default boolean isEmpty() {
        return size() == 0;
    }

    default <C extends Collection<E>> C ifEmpty(Supplier<C> collectionFactory) {
        return isEmpty() ? collectionFactory.get() : to(collectionFactory);
    }

    default boolean isNotEmpty() {
        return !isEmpty();
    }

    default boolean contains(E value) {
        return any(item -> item.equals(value));
    }

    default boolean containsNot(E e) {
        return !contains(e);
    }

    default boolean containsAll(@NotNull Iterable<E> iterable) {
        return Sequence.of(iterable).all(this::contains);
    }

    default ListX<E> plus(@NotNull E values) {
        final MutableListX<E> list = MutableListX.of(this);
        list.add(values);
        return list;
    }

    default ListX<E> plus(@NotNull Iterable<E> values) {
        final MutableListX<E> list = MutableListX.of(this);
        for (E value : values) {
            list.add(value);
        }
        return list;
    }

    default boolean containsNoneOf(@NotNull Iterable<E> iterable) {
        return Sequence.of(iterable).none(this::contains);
    }

    default <R> ListX<R> map(@NotNull Function<? super E, ? extends R> mapper) {
        return mapTo(MutableListX::empty, mapper);
    }

    default <R> ListX<R> mapIndexed(@NotNull BiFunction<Integer, ? super E, ? extends R> mapper) {
        return mapIndexedTo(MutableListX::empty, mapper);
    }

    default ListX<E> filter(@NotNull Predicate<E> predicate) {
        return filterTo(MutableListX::empty, predicate);
    }

    default <R> ListX<E> filterBy(@NotNull Function<? super E, ? extends R> selector, @NotNull Predicate<R> predicate) {
        return asSequence().filter(Objects::nonNull).filter(t -> predicate.test(selector.apply(t))).toListX();
    }

    default ListX<E> filterIndexed(@NotNull BiPredicate<Integer, E> predicate) {
        return filterIndexedTo(MutableListX::empty, predicate);
    }

    default ListX<E> filterNot(@NotNull Predicate<E> predicate) {
        return IterableX.super.filterNotTo(MutableListX::empty, predicate);
    }

    default <R> ListX<StringX> mapToStringX(@NotNull Function<? super E, ? extends R> mapper) {
        return map(s -> StringX.of(mapper.apply(s).toString()));
    }

    default <R> ListX<R> flatMap(@NotNull Function<E, Iterable<R>> mapper) {
        return flatMapTo(MutableListX::empty, mapper);
    }

    default <R> ListX<R> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<R>> mapper) {
        return mapMultiTo(MutableListX::empty, mapper);
    }

    default <R> ListX<R> mapNotNull(@NotNull Function<? super E, ? extends R> mapper) {
        return mapNotNullTo(MutableListX::empty, mapper);
    }

    @Override
    default <R> ListX<R> castIfInstanceOf(@NotNull Class<R> aClass) {
        return asSequence().filter(aClass::isInstance).map(aClass::cast).toListX();
    }

    default ListX<IndexedValue<E>> withIndex() {
        return Sequence.of(this::indexedIterator).toListX();
    }

    default IntSequence indices() {
        return asSequence().withIndex().mapToInt(IndexedValue::index);
    }

    @Override
    default ListX<E> sorted() {
        return (ListX<E>) IterableX.super.sorted();
    }

    @Override
    default <R extends Comparable<? super R>> ListX<E> sortedBy(@NotNull Function<? super E, ? extends R> selector) {
        return (ListX<E>) IterableX.super.sortedBy(selector);
    }

    @Override
    default ListX<E> sorted(Comparator<E> comparator) {
        return (ListX<E>) IterableX.super.sorted(comparator);
    }

    @Override
    default ListX<E> sortedDescending() {
        return (ListX<E>) IterableX.super.sortedDescending();
    }

    @Override
    default <R extends Comparable<? super R>> ListX<E> sortedByDescending(@NotNull Function<? super E, ? extends R> selector) {
        return (ListX<E>) IterableX.super.sortedByDescending(selector);
    }

    @Override
    default ListX<E> distinct() {
        return distinctBy(It::self);
    }

    @Override
    default <R> ListX<E> distinctBy(@NotNull Function<E, ? extends R> selector) {
        return distinctTo(MutableListX::empty, selector);
    }

    default ListX<ListX<E>> chunked(int size) {
        return windowed(size, size, true);
    }

    default ListX<ListX<E>> windowed(int size) {
        return windowed(size, 1);
    }

    default <R> ListX<R> windowed(int size, @NotNull Function<? super ListX<E>, ? extends R> transform) {
        return windowed(size, 1).map(transform);
    }

    default ListX<ListX<E>> windowed(int size, int step) {
        return windowed(size, step, false);
    }

    default <R> ListX<R> windowed(int size, int step, @NotNull Function<? super ListX<E>, ? extends R> transform) {
        return windowed(size, step, false).map(transform);
    }

    default ListX<ListX<E>> windowed(int size, boolean partialWindows) {
        return windowed(size, 1, partialWindows);
    }

    default ListX<ListX<E>> windowed(int size, int step, boolean partialWindows) {
        return windowed(size, step, partialWindows, It::self);
    }

    default <R> ListX<R> windowed(int size, int step, boolean partialWindows,
                                  @NotNull Function<? super ListX<E>, R> transform) {
        return asSequence().windowed(size, step, partialWindows).map(transform).toListX();
    }

    default <A, R> ListX<R> zip(@NotNull Iterable<A> iterable, @NotNull BiFunction<? super E, ? super A, ? extends R> function) {
        return zipTo(MutableListX::empty, iterable, function);
    }

    default <R> ListX<R> zipWithNext(@NotNull BiFunction<? super E, ? super E, ? extends R> function) {
        return zipWithNextTo(MutableListX::empty, function);
    }

    default <K> MapX<K, E> associateBy(@NotNull Function<? super E, ? extends K> keyMapper) {
        return toMutableMap(keyMapper, It::self);
    }

    default <V> MapX<E, V> associateWith(@NotNull Function<? super E, ? extends V> valueMapper) {
        return toMutableMap(It::self, valueMapper);
    }

    default ListX<E> skip(long count) {
        return skipTo(MutableListX::empty, (int) count);
    }

    @Override
    default ListX<E> skipWhile(@NotNull Predicate<E> predicate) {
        return skipWhileTo(MutableListX::empty, predicate, false);
    }

    @Override
    default ListX<E> skipWhileInclusive(@NotNull Predicate<E> predicate) {
        return skipWhileTo(MutableListX::empty, predicate, true);
    }

    @Override
    default ListX<E> take(long n) {
        return takeTo(MutableListX::empty, n);
    }

    default ListX<E> takeWhile(@NotNull Predicate<E> predicate) {
        return takeWhileTo(MutableListX::empty, predicate, false);
    }

    default ListX<E> takeWhileInclusive(@NotNull Predicate<E> predicate) {
        return takeWhileTo(MutableListX::empty, predicate, true);
    }

    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(iterator(), size(), Spliterator.SIZED);
    }
}
