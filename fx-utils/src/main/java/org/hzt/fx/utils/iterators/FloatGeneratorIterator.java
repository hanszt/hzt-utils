package org.hzt.fx.utils.iterators;

import org.hzt.fx.utils.function.FloatSupplier;
import org.hzt.fx.utils.function.FloatUnaryOperator;

import java.util.NoSuchElementException;

final class FloatGeneratorIterator implements FloatIterator {

    private final FloatSupplier initSupplier;
    private final FloatUnaryOperator nextValueSupplier;

    private float nextFloat;
    private State nextState = State.INIT_UNKNOWN;

    FloatGeneratorIterator(final FloatSupplier initSupplier, final FloatUnaryOperator nextValueSupplier) {
        this.initSupplier = initSupplier;
        this.nextValueSupplier = nextValueSupplier;
        this.nextFloat = initSupplier.getAsFloat();
    }

    @Override
    public boolean hasNext() {
        if (nextState.isUnknown()) {
            calculateNext();
        }
        return nextState == State.CONTINUE;
    }

    @Override
    public float nextFloat() {
        if (nextState.isUnknown()) {
            calculateNext();
        }
        if (nextState == State.DONE) {
            throw new NoSuchElementException();
        }
        final var result = nextFloat;
        // Do not clean nextItem (set item to 'null' to avoid keeping reference on yielded instance)
        // -- need to keep state for getNextValue
        nextState = State.NEXT_UNKNOWN;
        return result;
    }

    private void calculateNext() {
        nextFloat = nextState == State.INIT_UNKNOWN ? initSupplier.getAsFloat() : nextValueSupplier.applyAsFloat(nextFloat);
        nextState = State.CONTINUE;
    }
}
