package hzt.sequences;

import java.util.ListIterator;
import java.util.NoSuchElementException;

final class EmptyIterator<T> implements ListIterator<T> {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        throw new NoSuchElementException();
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public T previous() {
        throw new NoSuchElementException();
    }

    @Override
    public int nextIndex() {
        return 0;
    }

    @Override
    public int previousIndex() {
        return -1;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(T o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(T o) {
        throw new UnsupportedOperationException();
    }
}
