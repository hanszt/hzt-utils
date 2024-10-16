package org.hzt.utils.iterators.primitives;

import org.hzt.utils.iterators.State;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.DoublePredicate;

public final class DoubleTakeWhileIterator implements PrimitiveIterator.OfDouble {

    private final OfDouble iterator;
    private final DoublePredicate predicate;
    private final boolean inclusive;

    private boolean inclusiveConsumed = false;
    private double nextLong;
    private State nextState = State.INIT_UNKNOWN;

    private DoubleTakeWhileIterator(final OfDouble iterator, final DoublePredicate predicate, final boolean inclusive) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.inclusive = inclusive;
    }

    public static DoubleTakeWhileIterator of(final OfDouble iterator, final DoublePredicate predicate, final boolean inclusive) {
        return new DoubleTakeWhileIterator(iterator, predicate, inclusive);
    }

    public static DoubleTakeWhileIterator of(final OfDouble iterator, final DoublePredicate predicate) {
        return new DoubleTakeWhileIterator(iterator, predicate, false);
    }

    private void calculateNext() {
        if (iterator.hasNext()) {
            final double item = iterator.nextDouble();
            if (predicate.test(item) && !inclusiveConsumed) {
                nextState = State.CONTINUE;
                nextLong = item;
                return;
            }
            if (inclusive && !inclusiveConsumed && nextState != State.INIT_UNKNOWN) {
                nextState = State.CONTINUE;
                nextLong = item;
                inclusiveConsumed = true;
                return;
            }
        }
        nextState = State.DONE;
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
        final double result = nextLong;
        nextState = State.NEXT_UNKNOWN;
        return result;
    }
}
