package org.hzt.utils.iterators.primitives;

import org.hzt.utils.iterators.State;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.DoublePredicate;

public final class DoubleFilteringIterator implements PrimitiveIterator.OfDouble {

    private final OfDouble iterator;
    private final DoublePredicate predicate;
    private final boolean sendWhen;
    private State nextState = State.NEXT_UNKNOWN;
    private double nextDouble = 0.0;

    private DoubleFilteringIterator(final OfDouble iterator, final DoublePredicate predicate, final boolean sendWhen) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.sendWhen = sendWhen;
    }

    public static DoubleFilteringIterator of(final OfDouble iterator, final DoublePredicate predicate, final boolean sendWhen) {
        return new DoubleFilteringIterator(iterator, predicate, sendWhen);
    }

    @Override
    public boolean hasNext() {
        if (nextState == State.NEXT_UNKNOWN) {
            calculateNext();
        }
        return nextState == State.CONTINUE;
    }

    @Override
    public double nextDouble() {
        if (nextState == State.NEXT_UNKNOWN) {
            calculateNext();
        }
        if (nextState == State.DONE) {
            throw new NoSuchElementException();
        }
        final double result = nextDouble;
        nextDouble = 0L;
        nextState = State.NEXT_UNKNOWN;
        return result;
    }

    private void calculateNext() {
        while (iterator.hasNext()) {
            final double next = iterator.nextDouble();
            if (predicate.test(next) == sendWhen) {
                this.nextDouble = next;
                nextState = State.CONTINUE;
                return;
            }
        }
        nextState = State.DONE;
    }
}
