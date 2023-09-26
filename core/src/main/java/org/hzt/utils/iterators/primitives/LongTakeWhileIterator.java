package org.hzt.utils.iterators.primitives;

import org.hzt.utils.iterators.State;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.LongPredicate;

public final class LongTakeWhileIterator implements PrimitiveIterator.OfLong {

    private final OfLong iterator;
    private final LongPredicate predicate;
    private final boolean inclusive;

    private boolean inclusiveConsumed = false;
    private long nextLong;
    private State nextState = State.INIT_UNKNOWN;

    private LongTakeWhileIterator(final OfLong iterator, final LongPredicate predicate, final boolean inclusive) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.inclusive = inclusive;
    }

    public static LongTakeWhileIterator of(final OfLong iterator, final LongPredicate predicate, final boolean inclusive) {
        return new LongTakeWhileIterator(iterator, predicate, inclusive);
    }

    public static LongTakeWhileIterator of(final OfLong iterator, final LongPredicate predicate) {
        return new LongTakeWhileIterator(iterator, predicate, false);
    }

    private void calculateNext() {
        if (iterator.hasNext()) {
            final var item = iterator.nextLong();
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
    public long nextLong() {
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
