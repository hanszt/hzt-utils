package org.hzt.utils.iterators;

import java.util.Iterator;

public abstract class UnmodifiableIterator<T> implements Iterator<T> {

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
