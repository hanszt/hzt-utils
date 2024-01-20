package org.hzt.utils.iterators;

import org.hzt.utils.function.IndexedBiFunction;

import java.util.Iterator;

final class ScanningIterator<T, R> implements Iterator<R> {
    private final Iterator<T> iterator;
    private final IndexedBiFunction<? super R, ? super T, ? extends R> operation;

    private R accumulation;
    private int index = 0;

    private State state = State.INIT_UNKNOWN;

    ScanningIterator(final Iterator<T> iterator, final R initial, final IndexedBiFunction<? super R, ? super T, ? extends R> operation) {
        this.iterator = iterator;
        this.operation = operation;
        accumulation = initial;
    }

    @Override
    public boolean hasNext() {
        return state == State.INIT_UNKNOWN || iterator.hasNext();
    }

    @Override
    public R next() {
        if (state == State.INIT_UNKNOWN) {
            state = State.CONTINUE;
            return accumulation;
        }
        accumulation = operation.apply(index++, accumulation, iterator.next());
        return accumulation;
    }
}
