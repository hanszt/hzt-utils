package hzt.iterators.primitives;

import hzt.iterators.State;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;

public final class IntGeneratorIterator implements PrimitiveIterator.OfInt {

    private final IntSupplier initSupplier;
    private final IntUnaryOperator nextValueSupplier;

    private int nextInt;
    private State nextState = State.INIT_UNKNOWN;

    private IntGeneratorIterator(IntSupplier initSupplier, IntUnaryOperator nextValueSupplier) {
        this.initSupplier = initSupplier;
        this.nextValueSupplier = nextValueSupplier;
        this.nextInt = initSupplier.getAsInt();
    }

    public static PrimitiveIterator.OfInt of(IntSupplier initSupplier, IntUnaryOperator nextValueSupplier) {
        return new IntGeneratorIterator(initSupplier, nextValueSupplier);
    }

    @Override
    public boolean hasNext() {
        if (nextState.isUnknown()) {
            calculateNext();
        }
        return nextState == State.CONTINUE;
    }

    @Override
    public int nextInt() {
        if (nextState.isUnknown()) {
            calculateNext();
        }
        if (nextState == State.DONE) {
            throw new NoSuchElementException();
        }
        final int result = nextInt;
        // Do not clean nextItem (set item to 'null' to avoid keeping reference on yielded instance)
        // -- need to keep state for getNextValue
        nextState = State.NEXT_UNKNOWN;
        return result;
    }

    private void calculateNext() {
        nextInt = nextState == State.INIT_UNKNOWN ? initSupplier.getAsInt() : nextValueSupplier.applyAsInt(nextInt);
        nextState = State.CONTINUE;
    }
}
