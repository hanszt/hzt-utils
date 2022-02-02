package hzt.collections;

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
    public E[] toArray() {
        return array;
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator<>(array);
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }
}
