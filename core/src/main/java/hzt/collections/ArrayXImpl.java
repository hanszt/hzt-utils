package hzt.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
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

    @Override
    public Iterable<E> iterable() {
        return this::arrayIterator;
    }

    private @NotNull Iterator<E> arrayIterator() {
        return new Iterator<E>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public E next() {
                int prevIndex = index;
                if (prevIndex < 0 || prevIndex >= array.length) {
                    throw new NoSuchElementException("index out of bounds. (Index value: " + index + ")");
                }
                return array[index++];
            }
        };
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
        return IterableXHelper.binarySearch(array.length, i -> array[i], fromIndex, toIndex, comparison);
    }

    @Override
    public E[] toArray() {
        return array;
    }
}
