package org.hzt.utils.iterators.primitives;

import org.hzt.utils.iterators.State;

import java.util.PrimitiveIterator;
import java.util.function.IntBinaryOperator;

public class IntScanningIterator implements PrimitiveIterator.OfInt {
    private final PrimitiveIterator.OfInt iterator;
    private final IntBinaryOperator operation;

    private int accumulation;

    private State state = State.INIT_UNKNOWN;

    public IntScanningIterator(PrimitiveIterator.OfInt iterator, int initial, IntBinaryOperator operation) {
        this.iterator = iterator;
        this.operation = operation;
        accumulation = initial;
    }

    @Override
    public boolean hasNext() {
        return state == State.INIT_UNKNOWN || iterator.hasNext();
    }

    @Override
    public int nextInt() {
        if (state == State.INIT_UNKNOWN) {
            state = State.CONTINUE;
            return accumulation;
        }
        accumulation = operation.applyAsInt(accumulation, iterator.nextInt());
        return accumulation;
    }
}
