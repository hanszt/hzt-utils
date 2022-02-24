package hzt.iterables;

import hzt.collections.ListX;
import hzt.collections.MutableListX;
import hzt.collections.MutableSetX;
import hzt.tuples.IndexedValue;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class IterableXHelper {

    public static final Random RANDOM = new Random();

    private IterableXHelper() {
    }

    static <T, R extends Comparable<? super R>> ListX<T> toSortedListX(@NotNull Iterable<T> iterable,
                                                               @NotNull Function<? super T, ? extends R> selector) {
        return toMutableListXSortedBy(selector, iterable);
    }

    static <T> long count(Iterable<T> iterable, @NotNull Predicate<T> predicate) {
        long counter = 0;
        for (T t : iterable) {
            if (predicate.test(t)) {
                counter++;
            }
        }
        return counter;
    }

    static <T, R, C extends Collection<R>> C mapFilteringTo(
            @NotNull Iterable<T> iterable,
            @NotNull Supplier<C> collectionFactory,
            @NotNull Predicate<? super T> predicate,
            @NotNull Function<? super T, ? extends R> mapper,
            @NotNull Predicate<R> resultFilter) {
        C collection = collectionFactory.get();
        for (T t : iterable) {
            if (t != null && predicate.test(t)) {
                final R r = mapper.apply(t);
                if (resultFilter.test(r)) {
                    collection.add(r);
                }
            }
        }
        return collection;
    }

    @NotNull
    static <T, R extends Comparable<? super R>> Optional<T> compareBy(final Iterator<T> iterator,
            @NotNull Function<? super T, ? extends R> selector, @NotNull BiPredicate<R, R> biPredicate) {
        if (!iterator.hasNext()) {
            return Optional.empty();
        }
        T result = iterator.next();
        if (!iterator.hasNext()) {
            return Optional.ofNullable(result);
        }
        R value = result != null ? selector.apply(result) : null;
        do {
            final T next = iterator.next();
            final R nextToCompare = selector.apply(next);
            if (biPredicate.test(value, nextToCompare)) {
                result = next;
                value = nextToCompare;
            }
        } while (iterator.hasNext());
        return Optional.ofNullable(result);
    }

    static <R, K extends Comparable<? super K>> K asComparableOrThrow(R key) {
        if (key instanceof Comparable) {
            Comparable<?> c = (Comparable<?>) key;
            //noinspection unchecked
            return (K) c;
        }
        throw new IllegalStateException(key.getClass().getSimpleName() + " is not of a comparable type");
    }

    @NotNull
    static  <T, R extends Comparable<? super R>> R comparisonOf(
            @NotNull Iterator<T> iterator,
            @NotNull Function<? super T, ? extends R> selector,
            @NotNull BiPredicate<? super R, ? super R> biPredicate) {
        if (!iterator.hasNext()) {
            throw noValuePresentException();
        }
        final T first = iterator.next();
        R result = first != null ? selector.apply(first) : null;
        while (iterator.hasNext()) {
            final T next = iterator.next();
            if (next != null) {
                final R value = selector.apply(next);
                if (biPredicate.test(result, value)) {
                    result = value;
                }
            }
        }
        if (result != null) {
            return result;
        }
        throw noValuePresentException();
    }

    static <T> Optional<T> findLastIfUnknownIterable(Predicate<? super T> predicate, Iterator<T> iterator) {
        T result = iterator.next();
        if (!predicate.test(result) && !iterator.hasNext()) {
            return Optional.empty();
        }
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (predicate.test(next)) {
                result = next;
            }
        }
        return Optional.ofNullable(result);
    }

    static <T> Optional<T> findLastIfInstanceOfList(Predicate<? super T> predicate, List<T> list) {
        final T last = list.get(list.size() - 1);
        if (last != null && predicate.test(last)) {
            return Optional.of(last);
        }
        int index = list.size() - 2;
        while (index >= 0) {
            final T result = list.get(index);
            if (result != null && predicate.test(result)) {
                return Optional.of(result);
            }
            index--;
        }
        return Optional.empty();
    }

    public static double nextRandomDouble() {
        return RANDOM.nextDouble();
    }

    @NotNull
    static NoSuchElementException noValuePresentException() {
        return new NoSuchElementException("No value present");
    }

    static <T> void exposeIndexedNonNullVal(@NotNull Iterable<T> iterable, @NotNull BiConsumer<Integer, T> consumer) {
        int counter = 0;
        for (T value : iterable) {
            if (value != null) {
                consumer.accept(counter, value);
                counter++;
            }
        }
    }

    static <T> void exposeNonNullVal(@NotNull Iterable<T> iterable, @NotNull Consumer<T> consumer) {
        exposeIndexedNonNullVal(iterable, (i, v) -> consumer.accept(v));
    }

    public static <T> Iterator<IndexedValue<T>> indexedIterator(Iterator<T> iterator) {
        return new Iterator<IndexedValue<T>>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public IndexedValue<T> next() {
                int prevIndex = index;
                if (prevIndex < 0) {
                    throw new IllegalStateException("indexed iterator index overflow");
                }
                return new IndexedValue<>(index++, iterator.next());
            }
        };
    }

    static <T, R extends Comparable<? super R>> MutableListX<T> toMutableListXSortedBy(
            @NotNull Function<? super T, ? extends R> selector, Iterable<T> iterable) {
        MutableListX<T> list = MutableListX.of(iterable);
        list.sort(Comparator.comparing(selector));
        return list;
    }

    static <T, R> MutableSetX<R> toMutableSetNotNullOf(Iterable<T> iterable, @NotNull Function<? super T, ? extends R> transform) {
        return toCollectionNotNullOf(iterable, MutableSetX::empty, transform);
    }

    static <T, R, C extends Collection<R>> C toCollectionNotNullOf(Iterable<T> iterable,@NotNull Supplier<C> collectionFactory,
                                                                 @NotNull Function<? super T, ? extends R> mapper) {
        return mapFilteringTo(iterable, collectionFactory, Objects::nonNull, mapper, Objects::nonNull);
    }

    static <T, R> MutableListX<R> toMutableListXNotNullOf(Iterable<T> iterable, @NotNull Function<? super T, ? extends R> transform) {
        return IterableXHelper.toCollectionNotNullOf(iterable, MutableListX::empty, transform);
    }
}
