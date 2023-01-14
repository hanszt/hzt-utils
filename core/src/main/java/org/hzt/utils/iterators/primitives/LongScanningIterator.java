package org.hzt.utils.iterators.primitives;

import org.hzt.utils.iterators.State;

import java.util.PrimitiveIterator;
import java.util.function.LongBinaryOperator;

public class LongScanningIterator implements PrimitiveIterator.OfLong {
    private final PrimitiveIterator.OfLong iterator;
    private final LongBinaryOperator operation;

    private long accumulation;

    private State state = State.INIT_UNKNOWN;

    public LongScanningIterator(PrimitiveIterator.OfLong iterator, long initial, LongBinaryOperator operation) {
        this.iterator = iterator;
        this.operation = operation;
        accumulation = initial;
    }

    @Override
    public boolean hasNext() {
        return state == State.INIT_UNKNOWN || iterator.hasNext();
    }

    @Override
    public long nextLong() {
        if (state == State.INIT_UNKNOWN) {
            state = State.CONTINUE;
            return accumulation;
        }
        accumulation = operation.applyAsLong(accumulation, iterator.nextLong());
        return accumulation;
    }
}
