package org.hzt.fx.utils.iterators;

import org.hzt.fx.utils.function.FloatPredicate;

import java.util.NoSuchElementException;

final class FloatFilteringIterator implements FloatIterator {

    private final FloatIterator iterator;
    private final FloatPredicate predicate;
    private final boolean sendWhen;
    private State nextState = State.NEXT_UNKNOWN;
    private float nextFloat = 0.0F;

    FloatFilteringIterator(FloatIterator iterator, FloatPredicate predicate, boolean sendWhen) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.sendWhen = sendWhen;
    }

    public static FloatFilteringIterator of(FloatIterator iterator, FloatPredicate predicate, boolean sendWhen) {
        return new FloatFilteringIterator(iterator, predicate, sendWhen);
    }

    @Override
    public boolean hasNext() {
        if (nextState == State.NEXT_UNKNOWN) {
            calculateNext();
        }
        return nextState == State.CONTINUE;
    }

    @Override
    public float nextFloat() {
        if (nextState == State.NEXT_UNKNOWN) {
            calculateNext();
        }
        if (nextState == State.DONE) {
            throw new NoSuchElementException();
        }
        final float result = nextFloat;
        nextFloat = 0L;
        nextState = State.NEXT_UNKNOWN;
        return result;
    }

    private void calculateNext() {
        while (iterator.hasNext()) {
            float next = iterator.nextFloat();
            if (predicate.test(next) == sendWhen) {
                this.nextFloat = next;
                nextState = State.CONTINUE;
                return;
            }
        }
        nextState = State.DONE;
    }
}
