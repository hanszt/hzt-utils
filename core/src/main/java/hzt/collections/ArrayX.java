package hzt.collections;

import hzt.function.Transformable;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public interface ArrayX<E> extends IterableX<E>, Transformable<ArrayX<E>> {

    @SafeVarargs
    static <E> ArrayX<E> of(E... values) {
        return new ArrayXImpl<>(values);
    }

    static <E> ArrayX<E> ofNulls(int length) {
        return new ArrayXImpl<>(length, i -> null);
    }

    static <E> ArrayX<E> of(int length, IntFunction<E> generator) {
        return new ArrayXImpl<>(length, generator);
    }

    E get(int index);

    void set(int index, E value);

    int size();

    default  int binarySearchTo(int toIndex, ToIntFunction<E> comparison) {
        return binarySearch(0, toIndex, comparison);
    }

    default  int binarySearchFrom(int fromIndex, ToIntFunction<E> comparison) {
        return binarySearch(fromIndex, size(), comparison);
    }

    default  int binarySearch(ToIntFunction<E> comparison) {
        return binarySearch(0, size(), comparison);
    }

    int binarySearch(int fromIndex, int toIndex, ToIntFunction<E> comparison);

    E[] toArray();

    @Override
    default ArrayX<E> get() {
        return this;
    }
}
