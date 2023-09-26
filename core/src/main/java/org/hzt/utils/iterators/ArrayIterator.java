package org.hzt.utils.iterators;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

final class ArrayIterator<E> implements Iterator<E> {

    private final E[] array;

    private final boolean reverse;
    private int index;

    ArrayIterator(final E[] array, final boolean reverse) {
        this.array = Arrays.copyOf(array, array.length);
        this.reverse = reverse;
        this.index = reverse ? (array.length - 1) : 0;
    }

    @Override
    public boolean hasNext() {
        return reverse ? (index >= 0) : (index < array.length);
    }

    @Override
    public E next() {
        if (index < 0 || index >= array.length) {
            throw new NoSuchElementException("index out of bounds. (Index value: " + index + ")");
        }
        return array[reverse ? index-- : index++];
    }
}
