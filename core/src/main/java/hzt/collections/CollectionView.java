package hzt.collections;

import hzt.PreConditions;
import hzt.iterables.IterableX;
import hzt.iterables.IterableXHelper;
import hzt.ranges.IntRange;
import hzt.sequences.Sequence;
import hzt.strings.StringX;
import hzt.tuples.IndexedValue;
import hzt.tuples.Pair;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
        return mapIndexedTo(MutableListX::of, mapper);
    }

    default ListX<E> filter(@NotNull Predicate<E> predicate) {
        return filterTo(MutableListX::of, predicate);
    }

    default <R> ListX<E> filterBy(@NotNull Function<? super E, ? extends R> selector, @NotNull Predicate<R> predicate) {
        return asSequence().filter(Objects::nonNull).filter(t -> predicate.test(selector.apply(t))).toListX();
    }

    default ListX<E> filterIndexed(@NotNull BiPredicate<Integer, E> predicate) {
        return filterIndexedTo(MutableListX::of, predicate);
    }

    default ListX<E> filterNot(@NotNull Predicate<E> predicate) {
        return filter(predicate.negate());
    }

    default <R> ListX<StringX> mapToStringX(@NotNull Function<? super E, ? extends R> mapper) {
        return map(s -> StringX.of(mapper.apply(s).toString()));
    }

    default <R> ListX<R> flatMap(@NotNull Function<E, Iterable<R>> mapper) {
        return flatMapTo(MutableListX::of, mapper);
    }

    default <R> ListX<R> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<R>> mapper) {
        return mapMultiTo(MutableListX::of, mapper);
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

    default IntRange indices() {
        return Sequence.of(this::indexIterator).asIntRange(It::asInt);
    }

    private @NotNull Iterator<Integer> indexIterator() {
        Iterator<E> iterator = iterator();
        return new Iterator<>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public Integer next() {
                int prevIndex = index;
                if (prevIndex < 0) {
                    throw new IllegalStateException("indexed iterator index overflow");
                }
                iterator.next();
                return index++;
            }
        };
    }

    @Override
    default ListX<E> shuffled() {
        return sortedBy(s -> IterableXHelper.nextRandomDouble());
    }

    @Override
    default ListX<E> sorted() {
        return (ListX<E>) IterableX.super.sorted();
    }

    @Override
    default <R extends Comparable<R>> ListX<E> sortedBy(@NotNull Function<? super E, ? extends R> selector) {
        return (ListX<E>) IterableX.super.sortedBy(selector);
    }

    @Override
    default ListX<E> sortedBy(Comparator<E> comparator) {
        return (ListX<E>) IterableX.super.sortedBy(comparator);
    }

    @Override
    default ListX<E> sortedDescending() {
        return ListX.of(IterableX.super.sortedDescending());
    }

    @Override
    default <R extends Comparable<R>> ListX<E> sortedByDescending(@NotNull Function<? super E, ? extends R> selector) {
        return ListX.of(IterableX.super.sortedByDescending(selector));
    }

    @Override
    default ListX<E> distinct() {
        return distinctBy(It::self);
    }

    default <R> ListX<E> distinctBy(@NotNull Function<E, ? extends R> selector) {
        return distinctToMutableListBy(selector);
    }

    private  <R> MutableListX<E> distinctToMutableListBy(@NotNull Function<? super E, ? extends R> selector) {
        MutableListX<E> result = MutableListX.empty();
        MutableSetX<R> set = MutableLinkedSetX.empty();
        for (E t : this) {
            if (t != null) {
                final R r = selector.apply(t);
                if (set.add(r)) {
                    result.add(t);
                }
            }
        }
        return result;
    }

    default ListX<ListX<E>> chunked(int size) {
        return windowed(size, size, true);
    }

    default <R> ListX<R> chunked(int size, @NotNull Function<? super ListX<E>, ? extends R> transform) {
        return windowed(size, size, true).map(transform);
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
        return zipToListXWith(iterable, function);
    }

    private <A, R> ListX<R> zipToListXWith(@NotNull Iterable<A> otherIterable,
                                          @NotNull BiFunction<? super E, ? super A, ? extends R> function) {
        final Iterator<A> otherIterator = otherIterable.iterator();
        final Iterator<E> iterator = iterator();
        final int resultListSize = Math.min(collectionSizeOrElse(this, 10),
                collectionSizeOrElse(otherIterable, 10));

        final MutableListX<R> list = MutableListX.withInitCapacity(resultListSize);
        while (iterator.hasNext() && otherIterator.hasNext()) {
            final E next = iterator.next();
            final A otherNext = otherIterator.next();
            list.add(function.apply(next, otherNext));
        }
        return list;
    }

    static <T> int collectionSizeOrElse(Iterable<T> iterable, @SuppressWarnings("SameParameterValue") int defaultSize) {
        return collectionSizeOrElseGet(iterable, () -> defaultSize);
    }

    static <T> int collectionSizeOrElseGet(Iterable<T> iterable, IntSupplier supplier) {
        return iterable instanceof Collection ? ((Collection<T>) iterable).size() : supplier.getAsInt();
    }

    default ListX<Pair<E, E>> zipWithNext() {
        return zipWithNextToMutableListOf(Pair::of);
    }

    default <R> ListX<R> zipWithNext(BiFunction<E, E, R> function) {
        return zipWithNextToMutableListOf(function);
    }

    default <K> MapX<K, E> associateBy(@NotNull Function<? super E, ? extends K> keyMapper) {
        return toMutableMap(keyMapper, It::self);
    }

    default <V> MapX<E, V> associateWith(@NotNull Function<? super E, ? extends V> valueMapper) {
        return toMutableMap(It::self, valueMapper);
    }

    private <R> MutableListX<R> zipWithNextToMutableListOf(BiFunction<E, E, R> function) {
        final Iterator<E> iterator = iterator();
        if (!iterator.hasNext()) {
            return MutableListX.empty();
        }
        final MutableListX<R> list = MutableListX.empty();
        E current = iterator.next();
        while (iterator.hasNext()) {
            final E next = iterator.next();
            list.add(function.apply(current, next));
            current = next;
        }
        return list;
    }

    default ListX<E> skip(long count) {
        return filterIndexedToCollection(MutableListX::empty, (i, t) -> i >= count);
    }

    private <C extends Collection<E>> C filterIndexedToCollection(@NotNull Supplier<C> collectionFactory,
                                                                   @NotNull BiPredicate<Integer, E> predicate) {
        C collection = collectionFactory.get();
        for (IndexedValue<E> item : withIndex()) {
            if (item != null && predicate.test(item.index(), item.value())) {
                collection.add(item.value());
            }
        }
        return collection;
    }

    @Override
    default ListX<E> skipWhile(@NotNull Predicate<E> predicate) {
        return skipToMutableListWhile(this, predicate, false);
    }

    @Override
    default ListX<E> skipWhileInclusive(@NotNull Predicate<E> predicate) {
        return skipToMutableListWhile(this, predicate, true);
    }

    private static <T> MutableListX<T> skipToMutableListWhile(
            Iterable<T> iterable,
            Predicate<? super T> predicate,
            boolean inclusive) {
        boolean yielding = false;
        MutableListX<T> list = MutableListX.empty();
        for (T item : iterable) {
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

    @Override
    default ListX<E> take(long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return MutableListX.empty();
        }
        final Iterable<E> iterable = this;
        if (iterable instanceof Collection) {
            Collection<E> c = (Collection<E>) iterable;
            if (n >= c.size()) {
                return MutableListX.of(c);
            }
            if (n == 1) {
                return MutableListX.of(first());
            }
        }
        int count = 0;
        MutableListX<E> list = MutableListX.empty();
        for (E t : this) {
            list.add(t);
            if (++count == n) {
                break;
            }
        }
        return list;
    }

    default ListX<E> takeWhile(@NotNull Predicate<E> predicate) {
        final MutableListX<E> list = MutableListX.empty();
        for (E item : this) {
            if (!predicate.test(item)) {
                break;
            }
            list.add(item);
        }
        return list;
    }

    default ListX<E> takeWhileInclusive(@NotNull Predicate<E> predicate) {
        MutableListX<E> list = MutableListX.empty();
        for (E item : this) {
            list.add(item);
            if (!predicate.test(item)) {
                break;
            }
        }
        return list;
    }
}
