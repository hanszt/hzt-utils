package hzt.collections;

import hzt.iterators.ArrayIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

final class ArrayXImpl<E> implements ArrayX<E> {

    private final E[] array;

    ArrayXImpl(E[] array) {
        this.array = array;
    }

    ArrayXImpl(int length, IntFunction<E> defaultValueSupplier) {
        //noinspection unchecked
        this.array = (E[]) new Object[length];
        for (int i = 0; i < array.length; i++) {
            array[i] = defaultValueSupplier.apply(i);
        }
    }

    public E get(int index) {
        return array[index];
    }

    public void set(int index, E value) {
        array[index] = value;
    }

    @Override
    public int size() {
        return array.length;
    }

    @Override
    public int binarySearch(int fromIndex, int toIndex, ToIntFunction<E> comparison) {
        return ArrayHelper.binarySearch(array.length, i -> array[i], fromIndex, toIndex, comparison);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArrayXImpl<?> arrayX = (ArrayXImpl<?>) o;
        return Arrays.equals(array, arrayX.array);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }

    @Override
    public E[] toArray() {
        return Arrays.copyOf(array, array.length);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return ArrayIterator.of(array);
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }
}
