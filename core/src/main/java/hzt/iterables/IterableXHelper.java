package hzt.iterables;

import hzt.collections.IndexedValue;
import hzt.collections.MutableListX;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

public final class IterableXHelper {

    private IterableXHelper() {
    }
    static <T, R> MutableListX<T> filterToMutableListBy(Iterable<T> iterable,
                                                        Function<? super T, ? extends R> function,
                                                        Predicate<R> predicate,
                                                        Predicate<R> nullPredicate) {
        MutableListX<T> list = MutableListX.empty();
        for (T t : iterable) {
            if (t != null) {
                final R r = function.apply(t);
                if (nullPredicate.test(r) && predicate.test(r)) {
                    list.add(t);
                }
            }
        }
        return list;
    }

    @NotNull
    static <T, R extends Comparable<R>> Optional<T> compareBy(final Iterator<T> iterator,
            @NotNull Function<T, R> selector, @NotNull BiPredicate<R, R> biPredicate) {
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

    static <R, K extends Comparable<K>> K asComparableOrThrow(R key) {
        if (key instanceof Comparable) {
            Comparable<?> c = (Comparable<?>) key;
            //noinspection unchecked
            return (K) c;
        } else {
            throw new IllegalStateException(key.getClass().getSimpleName() + " is not of a comparable type");
        }
    }

    static <T> Iterator<Integer> indexIterator(Iterator<T> iterator) {
        return new Iterator<Integer>() {
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

    @NotNull
    static  <T, R extends Comparable<? extends R>> R comparisonOf(
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

    static <T> MutableListX<T> skipToMutableListWhile(
            Iterable<T> iterable,
            Predicate<? super T> predicate,
            boolean exclusive) {
        boolean yielding = false;
        MutableListX<T> list = MutableListX.empty();
        for (T item : iterable) {
            if (yielding) {
                list.add(item);
                continue;
            }
            if (!predicate.test(item)) {
                if (!exclusive) {
                    list.add(item);
                }
                yielding = true;
            }
        }
        return list;
    }

    static <T> int collectionSizeOrElse(Iterable<T> iterable, @SuppressWarnings("SameParameterValue") int defaultSize) {
        return collectionSizeOrElseGet(iterable, () -> defaultSize);
    }

    static <T> int collectionSizeOrElseGet(Iterable<T> iterable, IntSupplier supplier) {
        return iterable instanceof Collection ? ((Collection<T>) iterable).size() : supplier.getAsInt();
    }

    @NotNull
    static NoSuchElementException noValuePresentException() {
        return new NoSuchElementException("No value present");
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
}
