package org.hzt.utils.iterables;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class IterableXHelper {

    public static final Random RANDOM = new Random();

    private IterableXHelper() {
    }

    static <T> long count(Iterable<T> iterable, @NotNull Predicate<T> predicate) {
        long counter = 0;
        for (var t : iterable) {
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
        var collection = collectionFactory.get();
        for (var t : iterable) {
            if (t != null && predicate.test(t)) {
                final var r = mapper.apply(t);
                if (resultFilter.test(r)) {
                    collection.add(r);
                }
            }
        }
        return collection;
    }

    @NotNull
    static <T, R extends Comparable<? super R>> Optional<T> compareBy(final Iterator<T> iterator,
            @NotNull Function<? super T, ? extends R> selector, @NotNull IntPredicate predicate) {
        if (!iterator.hasNext()) {
            return Optional.empty();
        }
        var result = iterator.next();
        if (!iterator.hasNext()) {
            return Optional.ofNullable(result);
        }
        var value = result != null ? selector.apply(result) : null;
        do {
            final var next = iterator.next();
            final var nextToCompare = selector.apply(next);
            if (value != null && nextToCompare != null && predicate.test(value.compareTo(nextToCompare))) {
                result = next;
                value = nextToCompare;
            }
        } while (iterator.hasNext());
        return Optional.ofNullable(result);
    }

    public static <R, K extends Comparable<? super K>> K asComparableOrThrow(R key) {
        if (key instanceof Comparable) {
            var c = (Comparable<?>) key;
            //noinspection unchecked
            return (K) c;
        }
        throw new IllegalStateException(key.getClass().getSimpleName() + " is not of a comparable type");
    }

    @NotNull
    static  <T, R extends Comparable<? super R>> R comparisonOf(
            @NotNull Iterator<T> iterator,
            @NotNull Function<? super T, ? extends R> selector,
            @NotNull IntPredicate biPredicate) {
        if (!iterator.hasNext()) {
            throw noValuePresentException();
        }
        final var first = iterator.next();
        var result = first != null ? selector.apply(first) : null;
        while (iterator.hasNext()) {
            final var next = iterator.next();
            if (next != null) {
                final var value = selector.apply(next);
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

    static <T> Optional<T> findLastIfUnknownIterable(Predicate<? super T> predicate, Iterator<T> iterator) {
        var result = iterator.next();
        if (!predicate.test(result) && !iterator.hasNext()) {
            return Optional.empty();
        }
        while (iterator.hasNext()) {
            var next = iterator.next();
            if (predicate.test(next)) {
                result = next;
            }
        }
        return Optional.ofNullable(result);
    }

    static <T> Optional<T> findLastIfInstanceOfList(Predicate<? super T> predicate, List<T> list) {
        final var last = list.get(list.size() - 1);
        if (last != null && predicate.test(last)) {
            return Optional.of(last);
        }
        var index = list.size() - 2;
        while (index >= 0) {
            final var result = list.get(index);
            if (result != null && predicate.test(result)) {
                return Optional.of(result);
            }
            index--;
        }
        return Optional.empty();
    }

    @NotNull
    static NoSuchElementException noValuePresentException() {
        return new NoSuchElementException("No value present");
    }

    static <T> void exposeNonNullVal(@NotNull Iterable<T> iterable, @NotNull Consumer<T> consumer) {
        for (var value : iterable) {
            if (value != null) {
                consumer.accept(value);
            }
        }
    }

    static <T> void exposeNonNullVal(@NotNull Iterable<T> iterable, @NotNull Consumer<T> consumer) {
        exposeIndexedNonNullVal(iterable, (i, v) -> consumer.accept(v));
    }

    static <T, R extends Comparable<? super R>> MutableListX<T> toMutableListXSortedBy(
            @NotNull Function<? super T, ? extends R> selector, Iterable<T> iterable) {
        MutableListX<T> list = MutableListX.of(iterable);
        list.sort(Comparator.comparing(selector));
        return list;
    }
}
