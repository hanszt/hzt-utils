package hzt.collections;

import hzt.PreConditions;
import hzt.tuples.IndexedValue;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

final class CollectionsHelper {

    private CollectionsHelper() {
    }

    static <E, R, I extends Iterable<R>> MutableListX<R> flatMapToMutableListOf(
            @NotNull Iterable<E> iterable,
            @NotNull Function<? super E, ? extends I> mapper) {
        final MutableListX<R> list = MutableListX.empty();
        for (E t : iterable) {
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

    static <E, R> MutableListX<E> distinctToMutableListBy(@NotNull Iterable<E> iterable,
                                                          @NotNull Function<? super E, ? extends R> selector) {
        MutableListX<E> result = MutableListX.empty();
        MutableSetX<R> set = MutableLinkedSetX.empty();
        for (E t : iterable) {
            if (t != null) {
                final R r = selector.apply(t);
                if (set.add(r)) {
                    result.add(t);
                }
            }
        }
        return result;
    }

    static <E, R> MutableListX<R> zipWithNextToMutableListOf(Iterator<E> iterator, BiFunction<E, E, R> function) {
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

    static <E, C extends Collection<E>> C filterIndexedToCollection(
            @NotNull Iterable<IndexedValue<E>> indexedValues,
            @NotNull Supplier<C> collectionFactory,
            @NotNull BiPredicate<Integer, E> predicate) {
        C collection = collectionFactory.get();
        for (IndexedValue<E> item : indexedValues) {
            if (item != null && predicate.test(item.index(), item.value())) {
                collection.add(item.value());
            }
        }
        return collection;
    }

    static <T> MutableListX<T> skipToMutableListWhile(
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

    static <E> MutableListX<E> takeLastToMutableList(ListX<E> listX, int n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return MutableListX.empty();
        }
        MutableListX<E> list = MutableListX.of(listX);
        int size = list.size();
        if (n >= size) {
            return list;
        }
        if (n == 1) {
            return MutableListX.of(listX.last());
        }
        MutableListX<E> resultList = MutableListX.withInitCapacity(n);
        for (int index = size - n; index < size; index++) {
            resultList.add(list.get(index));
        }
        return resultList;
    }
}
