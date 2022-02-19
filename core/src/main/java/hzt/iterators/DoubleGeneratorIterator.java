package hzt.iterators;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;

public final class DoubleGeneratorIterator implements PrimitiveIterator.OfDouble {

    private final DoubleSupplier initSupplier;
    private final DoubleUnaryOperator nextValueSupplier;

    private double nextDouble;
    private State nextState = State.INIT_UNKNOWN;

    private DoubleGeneratorIterator(DoubleSupplier initSupplier, DoubleUnaryOperator nextValueSupplier) {
        this.initSupplier = initSupplier;
        this.nextValueSupplier = nextValueSupplier;
        this.nextDouble = initSupplier.getAsDouble();
    }

    public static OfDouble of(DoubleSupplier initSupplier, DoubleUnaryOperator nextValueSupplier) {
        return new DoubleGeneratorIterator(initSupplier, nextValueSupplier);
    }

    @Override
    public boolean hasNext() {
        if (nextState.isUnknown()) {
            calculateNext();
        }
        return nextState == State.CONTINUE;
    }

    @Override
    public double nextDouble() {
        if (nextState.isUnknown()) {
            calculateNext();
        }
        if (nextState == State.DONE) {
            throw new NoSuchElementException();
        }
        final double result = nextDouble;
        // Do not clean nextItem (set item to 'null' to avoid keeping reference on yielded instance)
        // -- need to keep state for getNextValue
        nextState = State.NEXT_UNKNOWN;
        return result;
    }

    private void calculateNext() {
        nextDouble = nextState == State.INIT_UNKNOWN ? initSupplier.getAsDouble() : nextValueSupplier.applyAsDouble(nextDouble);
        nextState = State.CONTINUE;
    }
}
