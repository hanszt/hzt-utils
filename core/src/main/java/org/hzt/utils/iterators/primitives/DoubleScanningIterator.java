package org.hzt.utils.iterators.primitives;

import org.hzt.utils.iterators.State;

import java.util.PrimitiveIterator;
import java.util.function.DoubleBinaryOperator;

public class DoubleScanningIterator implements PrimitiveIterator.OfDouble {
    private final PrimitiveIterator.OfDouble iterator;
    private final DoubleBinaryOperator operation;
    private double accumulation;

    private State state = State.INIT_UNKNOWN;

    public DoubleScanningIterator(final PrimitiveIterator.OfDouble iterator, final double initial,
                                  final DoubleBinaryOperator operation) {
        this.iterator = iterator;
        this.operation = operation;
        accumulation = initial;
    }

    @Override
    public boolean hasNext() {
        return state == State.INIT_UNKNOWN || iterator.hasNext();
    }

    @Override
    public double nextDouble() {
        if (state == State.INIT_UNKNOWN) {
            state = State.CONTINUE;
            return accumulation;
        }
        accumulation = operation.applyAsDouble(accumulation, iterator.nextDouble());
        return accumulation;
    }
}
