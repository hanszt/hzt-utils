package hzt.collections;

import hzt.PreConditions;
import hzt.iterables.IterableX;
import hzt.ranges.IntRange;
import hzt.sequences.Sequence;
import hzt.strings.StringX;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

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

    default ListX<E> plus(@NotNull E... values) {
        final MutableListX<E> list = MutableListX.of(this);
        Collections.addAll(list, values);
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
        return toMutableListOf(mapper);
    }

    default <R> ListX<R> mapIndexed(@NotNull BiFunction<Integer, ? super E, ? extends R> mapper) {
        return mapIndexedToMutableList(mapper);
    }

    private  <R> MutableListX<R> mapIndexedToMutableList(@NotNull BiFunction<Integer, ? super E, ? extends R> mapper) {
        return withIndex().mapTo(MutableListX::empty, indexedValue -> mapper.apply(indexedValue.index(), indexedValue.value()));
    }

    default ListX<E> filter(@NotNull Predicate<E> predicate) {
        return asSequence().filter(predicate).toListX();
    }

    default <R> ListX<E> filterBy(@NotNull Function<? super E, ? extends R> selector, @NotNull Predicate<R> predicate) {
        return asSequence().filter(Objects::nonNull).filter(t -> predicate.test(selector.apply(t))).toListX();
    }

    default ListX<E> filterIndexed(@NotNull BiPredicate<Integer, E> predicate) {
        return asSequence().filterIndexed(predicate).toListX();
    }

    default ListX<E> filterNot(@NotNull Predicate<E> predicate) {
        return filter(predicate.negate());
    }

    default <R> ListX<StringX> mapToStringX(@NotNull Function<? super E, ? extends R> mapper) {
        return map(s -> StringX.of(mapper.apply(s).toString()));
    }

    default <R> ListX<R> flatMap(@NotNull Function<E, Iterable<R>> mapper) {
        return flatMapToMutableListOf(mapper);
    }

    private <R, I extends Iterable<R>> MutableListX<R> flatMapToMutableListOf(@NotNull Function<? super E, ? extends I> mapper) {
        final MutableListX<R> list = MutableListX.empty();
        for (E t : this) {
            final I c = mapper.apply(t);
            if (c == null) {
                continue;
            }
            for (R r : c) {
                if (r != null) {
                    list.add(r);
                }
            }
        }
        return list;
    }

    default <R> ListX<R> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<R>> mapper) {
        MutableListX<R> list = MutableListX.empty();
        for (E t : this) {
            mapper.accept(t, (Consumer<R>) list::add);
        }
        return list;
    }

    default <R> ListX<R> mapNotNull(@NotNull Function<? super E, ? extends R> mapper) {
        return toListXOf(mapper);
    }

    @Override
    default <R> ListX<R> castIfInstanceOf(@NotNull Class<R> aClass) {
        return asSequence().filter(aClass::isInstance).map(aClass::cast).toListX();
    }

    default ListX<IndexedValue<E>> withIndex() {
        return Sequence.of(this::indexedIterator).toListX();
    }

    default IntRange indices() {
        return Sequence.of(() -> indexIterator(iterator())).asIntRange(It::asInt);
    }

    static <T> Iterator<Integer> indexIterator(Iterator<T> iterator) {
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

    default ListX<E> sorted() {
        return IterableX.super.toSortedListX();
    }

    default <R extends Comparable<R>> ListX<E> sortedBy(@NotNull Function<? super E, ? extends R> selector) {
        return toSortedListX(selector);
    }

    @Override
    default <R extends Comparable<R>> ListX<E> sortedByDescending(@NotNull Function<? super E, ? extends R> selector) {
        return toSortedListX(selector);
    }

    default IterableX<E> distinct() {
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

    default <A, R> ListX<R> zipWith(@NotNull Iterable<A> iterable, @NotNull BiFunction<? super E, ? super A, ? extends R> function) {
        return ListX.of(zipToListWith(iterable, function));
    }

    default <R> ListX<R> zipWithNext(BiFunction<E, E, R> function) {
        return zipWithNextToMutableListOf(function);
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

    @Override
    @NotNull
    default ListX<E> onEach(@NotNull Consumer<? super E> consumer) {
        return onEach(It::self, consumer);
    }

    @Override
    @NotNull
    default <R> ListX<E> onEach(@NotNull Function<? super E, ? extends R> selector,
                                    @NotNull Consumer<? super R> consumer) {
        MutableListX<E> listX = MutableListX.empty();
        for (E t : this) {
            consumer.accept(t != null ? selector.apply(t) : null);
            listX.add(t);
        }
        return listX;
    }
}
