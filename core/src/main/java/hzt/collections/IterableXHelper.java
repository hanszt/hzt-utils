package hzt.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

final class IterableXHelper {

    private IterableXHelper() {
    }
    static <T, R> MutableListX<T> filterToMutableListBy(Iterable<T> iterable,
            Function<? super T, ? extends R> function, Predicate<R> predicate, Predicate<R> nullPredicate) {
        MutableListX<T> list = MutableListX.<T>empty();
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
    static  <T, R extends Comparable<R>> R comparisonOf(
            Iterator<T> iterator, @NotNull Function<T, R> selector, @NotNull BiPredicate<R, R> biPredicate) {
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

    static <T> Optional<T> findLastIfUnknownIterable(Predicate<T> predicate, Iterator<T> iterator) {
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

    static <T> Optional<T> findLastIfInstanceOfList(Predicate<T> predicate, List<T> list) {
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

    static <T> MutableListX<T> dropToMutableListWhile(Iterable<T> iterable, Predicate<T> predicate, boolean exclusive) {
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

    static <T> int binarySearch(
            int size, IntFunction<T> midValExtractor, int fromIndex, int toIndex, ToIntFunction<T> comparison) {
        rangeCheck(size, fromIndex, toIndex);

        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final T midVal = midValExtractor.apply(mid);
            final int cmp = comparison.applyAsInt(midVal);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }
        return -(low + 1);  // key not found
    }

    static int binarySearch(
            int size, IntUnaryOperator midValExtractor, int fromIndex, int toIndex, IntUnaryOperator comparison) {
        rangeCheck(size, fromIndex, toIndex);

        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final int midVal = midValExtractor.applyAsInt(mid);
            final int cmp = comparison.applyAsInt(midVal);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }
        return -(low + 1);  // key not found
    }

    private static void rangeCheck(int size, int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex (" + fromIndex + ") is greater than toIndex (" + toIndex + ").");
        }
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException("fromIndex (" + fromIndex + ") is less than zero.");
        }
        if (toIndex > size) {
            throw new IndexOutOfBoundsException("toIndex (" + toIndex + ") is greater than size (" + size + ").");
        }
    }
}
