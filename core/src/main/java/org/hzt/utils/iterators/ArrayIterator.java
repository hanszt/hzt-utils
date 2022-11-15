package org.hzt.utils.iterators;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

final class ArrayIterator<E> implements Iterator<E> {

    private final E[] array;
    private int index = 0;

    ArrayIterator(E[] array) {
        this.array = Arrays.copyOf(array, array.length);
    }

    @Override
    public boolean hasNext() {
        return index < array.length;
    }

    @Override
    public E next() {
        if (index < 0 || index >= array.length) {
            throw new NoSuchElementException("index out of bounds. (Index value: " + index + ")");
        }
        return array[index++];
    }
}
