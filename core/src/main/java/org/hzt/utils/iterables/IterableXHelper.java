package org.hzt.utils.iterables;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MutableListX;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class IterableXHelper {

    private IterableXHelper() {
    }

    static <T, R extends Comparable<? super R>> ListX<T> toSortedListX(final Iterable<T> iterable,
                                                                       final Function<? super T, ? extends R> selector) {
        return toMutableListXSortedBy(selector, iterable);
    }

    static <T> long count(final Iterable<T> iterable, final Predicate<T> predicate) {
        long counter = 0;
        for (final T t : iterable) {
            if (predicate.test(t)) {
                counter++;
            }
        }
        return counter;
    }

    static <T, R, C extends Collection<R>> C mapFilteringTo(
            final Iterable<T> iterable,
            final Supplier<C> collectionFactory,
            final Predicate<? super T> predicate,
            final Function<? super T, ? extends R> mapper,
            final Predicate<R> resultFilter) {
        final C collection = collectionFactory.get();
        for (final T t : iterable) {
            if (t != null && predicate.test(t)) {
                final R r = mapper.apply(t);
                if (resultFilter.test(r)) {
                    collection.add(r);
                }
            }
        }
        return collection;
    }

    static <T, R extends Comparable<? super R>> Optional<T> compareBy(final Iterator<T> iterator,
                                                                      final Function<? super T, ? extends R> selector, final IntPredicate predicate) {
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
            if (value != null && nextToCompare != null && predicate.test(value.compareTo(nextToCompare))) {
                result = next;
                value = nextToCompare;
            }
        } while (iterator.hasNext());
        return Optional.ofNullable(result);
    }

    public static <R, K extends Comparable<? super K>> K asComparableOrThrow(final R key) {
        if (key instanceof Comparable) {
            final Comparable<?> c = (Comparable<?>) key;
            //noinspection unchecked
            return (K) c;
        }
        throw new IllegalStateException(key.getClass().getSimpleName() + " is not of a comparable type");
    }

    static  <T, R extends Comparable<? super R>> R comparisonOf(
            final Iterator<T> iterator,
            final Function<? super T, ? extends R> selector,
            final IntPredicate biPredicate) {
        if (!iterator.hasNext()) {
            throw noValuePresentException();
        }
        final T first = iterator.next();
        R result = first != null ? selector.apply(first) : null;
        while (iterator.hasNext()) {
            final T next = iterator.next();
            if (next != null) {
                final R value = selector.apply(next);
                if (result != null && value != null && biPredicate.test(result.compareTo(value))) {
                    result = value;
                }
            }
        }
        if (result != null) {
            return result;
        }
        throw noValuePresentException();
    }

    static <T> Optional<T> findLastIfUnknownIterable(final Predicate<? super T> predicate, final Iterator<T> iterator) {
        T result = iterator.next();
        if (!predicate.test(result) && !iterator.hasNext()) {
            return Optional.empty();
        }
        while (iterator.hasNext()) {
            final T next = iterator.next();
            if (predicate.test(next)) {
                result = next;
            }
        }
        return Optional.ofNullable(result);
    }

    static <T> Optional<T> findLastIfInstanceOfList(final Predicate<? super T> predicate, final List<T> list) {
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

    static NoSuchElementException noValuePresentException() {
        return new NoSuchElementException("No value present");
    }

    static <T> void exposeIndexedNonNullVal(final Iterable<T> iterable, final BiConsumer<Integer, T> consumer) {
        int counter = 0;
        for (final T value : iterable) {
            if (value != null) {
                consumer.accept(counter, value);
                counter++;
            }
        }
    }

    static <T> void exposeNonNullVal(final Iterable<T> iterable, final Consumer<T> consumer) {
        exposeIndexedNonNullVal(iterable, (i, v) -> consumer.accept(v));
    }

    static <T, R extends Comparable<? super R>> MutableListX<T> toMutableListXSortedBy(
            final Function<? super T, ? extends R> selector, final Iterable<T> iterable) {
        final MutableListX<T> list = MutableListX.of(iterable);
        list.sort(Comparator.comparing(selector));
        return list;
    }
}
