package hzt.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayIterator<E> implements Iterator<E> {

    private final E[] array;
    private int index = 0;

    public ArrayIterator(E[] array) {
        this.array = array;
    }

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
}
