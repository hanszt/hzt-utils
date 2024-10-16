package org.hzt.utils.iterators.primitives;

import org.hzt.utils.iterators.State;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.LongSupplier;
import java.util.function.LongUnaryOperator;

final class LongGeneratorIterator implements PrimitiveIterator.OfLong {

    private final LongSupplier initSupplier;
    private final LongUnaryOperator nextValueSupplier;

    private long nextLong;
    private State nextState = State.INIT_UNKNOWN;

    LongGeneratorIterator(final LongSupplier initSupplier, final LongUnaryOperator nextValueSupplier) {
        this.initSupplier = initSupplier;
        this.nextValueSupplier = nextValueSupplier;
        this.nextLong = initSupplier.getAsLong();
    }

    @Override
    public boolean hasNext() {
        if (nextState.isUnknown()) {
            calculateNext();
        }
        return nextState == State.CONTINUE;
    }

    @Override
    public long nextLong() {
        if (nextState.isUnknown()) {
            calculateNext();
        }
        if (nextState == State.DONE) {
            throw new NoSuchElementException();
        }
        final long result = nextLong;
        // Do not clean nextItem (set item to 'null' to avoid keeping reference on yielded instance)
        // -- need to keep state for getNextValue
        nextState = State.NEXT_UNKNOWN;
        return result;
    }

    private void calculateNext() {
        nextLong = nextState == State.INIT_UNKNOWN ? initSupplier.getAsLong() : nextValueSupplier.applyAsLong(nextLong);
        nextState = State.CONTINUE;
    }
}
