package org.hzt.utils.iterators;

import java.util.Iterator;
import java.util.function.BiFunction;

public class ScanningIterator<T, R> implements Iterator<R> {
    private final Iterator<T> iterator;
    private final BiFunction<? super R, ? super T, ? extends R> operation;

    private R accumulation;

    private State state = State.INIT_UNKNOWN;

    public ScanningIterator(final Iterator<T> iterator, final R initial, final BiFunction<? super R, ? super T, ? extends R> operation) {
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
        accumulation = operation.apply(accumulation, iterator.next());
        return accumulation;
    }
}
