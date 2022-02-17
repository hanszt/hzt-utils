package hzt.collections;

import hzt.iterables.IterableX;
import hzt.iterables.IterableXHelper;
import hzt.ranges.IntRange;
import hzt.sequences.Sequence;
import hzt.strings.StringX;
import hzt.tuples.IndexedValue;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface CollectionView<E> extends IterableX<E> {

    default int size() {
        return (int) count(It::noFilter);
    }

    default boolean isEmpty() {
        return size() == 0;
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

    default ListView<E> plus(@NotNull E values) {
        final MutableList<E> list = MutableList.of(this);
        list.add(values);
        return list;
    }

    default ListView<E> plus(@NotNull Iterable<E> values) {
        final MutableList<E> list = MutableList.of(this);
        for (E value : values) {
            list.add(value);
        }
        return list;
    }

    default boolean containsNoneOf(@NotNull Iterable<E> iterable) {
        return Sequence.of(iterable).none(this::contains);
    }

    default <R> ListView<R> map(@NotNull Function<? super E, ? extends R> mapper) {
        return mapTo(MutableList::empty, mapper);
    }

    default <R> ListView<R> mapIndexed(@NotNull BiFunction<Integer, ? super E, ? extends R> mapper) {
        return mapIndexedTo(MutableList::empty, mapper);
    }

    default ListView<E> filter(@NotNull Predicate<E> predicate) {
        return filterTo(MutableList::empty, predicate);
    }

    default <R> ListView<E> filterBy(@NotNull Function<? super E, ? extends R> selector, @NotNull Predicate<R> predicate) {
        return asSequence().filter(Objects::nonNull).filter(t -> predicate.test(selector.apply(t))).toListView();
    }

    default ListView<E> filterIndexed(@NotNull BiPredicate<Integer, E> predicate) {
        return filterIndexedTo(MutableList::empty, predicate);
    }

    default ListView<E> filterNot(@NotNull Predicate<E> predicate) {
        return IterableX.super.filterNotTo(MutableList::empty, predicate);
    }

    default <R> ListView<StringX> mapToStringX(@NotNull Function<? super E, ? extends R> mapper) {
        return map(s -> StringX.of(mapper.apply(s).toString()));
    }

    default <R> ListView<R> flatMap(@NotNull Function<E, Iterable<R>> mapper) {
        return flatMapTo(MutableList::empty, mapper);
    }

    default <R> ListView<R> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<R>> mapper) {
        return mapMultiTo(MutableList::empty, mapper);
    }

    default <R> ListView<R> mapNotNull(@NotNull Function<? super E, ? extends R> mapper) {
        return mapNotNullTo(MutableList::empty, mapper);
    }

    @Override
    default <R> ListView<R> castIfInstanceOf(@NotNull Class<R> aClass) {
        return asSequence().filter(aClass::isInstance).map(aClass::cast).toListView();
    }

    default ListView<IndexedValue<E>> withIndex() {
        return Sequence.of(this::indexedIterator).toListView();
    }

    default IntRange indices() {
        return asSequence().withIndex().asIntRange(IndexedValue::index);
    }

    @Override
    default ListView<E> shuffled() {
        return sortedBy(s -> IterableXHelper.nextRandomDouble());
    }

    @Override
    default ListView<E> sorted() {
        return (ListView<E>) IterableX.super.sorted();
    }

    @Override
    default <R extends Comparable<R>> ListView<E> sortedBy(@NotNull Function<? super E, ? extends R> selector) {
        return (ListView<E>) IterableX.super.sortedBy(selector);
    }

    @Override
    default ListView<E> sortedBy(Comparator<E> comparator) {
        return (ListView<E>) IterableX.super.sortedBy(comparator);
    }

    @Override
    default ListView<E> sortedDescending() {
        return (ListView<E>) IterableX.super.sortedDescending();
    }

    @Override
    default <R extends Comparable<R>> ListView<E> sortedByDescending(@NotNull Function<? super E, ? extends R> selector) {
        return (ListView<E>) IterableX.super.sortedByDescending(selector);
    }

    @Override
    default ListView<E> distinct() {
        return distinctBy(It::self);
    }

    @Override
    default <R> ListView<E> distinctBy(@NotNull Function<E, ? extends R> selector) {
        return distinctTo(MutableList::empty, selector);
    }

    default ListView<ListView<E>> chunked(int size) {
        return windowed(size, size, true);
    }

    default <R> ListView<R> chunked(int size, @NotNull Function<? super ListView<E>, ? extends R> transform) {
        return windowed(size, size, true).map(transform);
    }

    default ListView<ListView<E>> windowed(int size) {
        return windowed(size, 1);
    }

    default <R> ListView<R> windowed(int size, @NotNull Function<? super ListView<E>, ? extends R> transform) {
        return windowed(size, 1).map(transform);
    }

    default ListView<ListView<E>> windowed(int size, int step) {
        return windowed(size, step, false);
    }

    default <R> ListView<R> windowed(int size, int step, @NotNull Function<? super ListView<E>, ? extends R> transform) {
        return windowed(size, step, false).map(transform);
    }

    default ListView<ListView<E>> windowed(int size, boolean partialWindows) {
        return windowed(size, 1, partialWindows);
    }

    default ListView<ListView<E>> windowed(int size, int step, boolean partialWindows) {
        return windowed(size, step, partialWindows, It::self);
    }

    default <R> ListView<R> windowed(int size, int step, boolean partialWindows,
                                     @NotNull Function<? super ListView<E>, R> transform) {
        return asSequence().windowed(size, step, partialWindows).map(transform).toListView();
    }

    default <A, R> ListView<R> zip(@NotNull Iterable<A> iterable, @NotNull BiFunction<? super E, ? super A, ? extends R> function) {
        return zipTo(MutableList::empty, iterable, function);
    }

    default <R> ListView<R> zipWithNext(BiFunction<E, E, R> function) {
        return zipWithNextTo(MutableList::empty, function);
    }

    default <K> MapView<K, E> associateBy(@NotNull Function<? super E, ? extends K> keyMapper) {
        return toMutableMap(keyMapper, It::self);
    }

    default <V> MapView<E, V> associateWith(@NotNull Function<? super E, ? extends V> valueMapper) {
        return toMutableMap(It::self, valueMapper);
    }

    default ListView<E> skip(long count) {
        return skipTo(MutableList::empty, (int) count);
    }

    @Override
    default ListView<E> skipWhile(@NotNull Predicate<E> predicate) {
        return skipWhileTo(MutableList::empty, predicate, false);
    }

    @Override
    default ListView<E> skipWhileInclusive(@NotNull Predicate<E> predicate) {
        return skipWhileTo(MutableList::empty, predicate, true);
    }

    @Override
    default ListView<E> take(long n) {
        return takeTo(MutableList::empty, n);
    }

    default ListView<E> takeWhile(@NotNull Predicate<E> predicate) {
        return takeWhileTo(MutableList::empty, predicate, false);
    }

    default ListView<E> takeWhileInclusive(@NotNull Predicate<E> predicate) {
        return takeWhileTo(MutableList::empty, predicate, true);
    }
}
