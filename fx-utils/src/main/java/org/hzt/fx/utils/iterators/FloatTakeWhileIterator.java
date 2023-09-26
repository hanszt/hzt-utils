package org.hzt.fx.utils.iterators;

import org.hzt.fx.utils.function.FloatPredicate;

import java.util.NoSuchElementException;

final class FloatTakeWhileIterator implements FloatIterator {

    private final FloatIterator iterator;
    private final FloatPredicate predicate;
    private final boolean inclusive;

    private boolean inclusiveConsumed = false;
    private float nextLong;
    private State nextState = State.INIT_UNKNOWN;

    private FloatTakeWhileIterator(final FloatIterator iterator, final FloatPredicate predicate, final boolean inclusive) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.inclusive = inclusive;
    }

    public static FloatTakeWhileIterator of(final FloatIterator iterator, final FloatPredicate predicate, final boolean inclusive) {
        return new FloatTakeWhileIterator(iterator, predicate, inclusive);
    }

    public static FloatTakeWhileIterator of(final FloatIterator iterator, final FloatPredicate predicate) {
        return new FloatTakeWhileIterator(iterator, predicate, false);
    }

    private void calculateNext() {
        if (iterator.hasNext()) {
            final var item = iterator.nextFloat();
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
    public float nextFloat() {
        if (nextState.isUnknown()) {
            calculateNext();
        }
        if (nextState == State.DONE) {
            throw new NoSuchElementException();
        }
        final var result = nextLong;
        nextState = State.NEXT_UNKNOWN;
        return result;
    }
}
