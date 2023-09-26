package org.hzt.utils.iterators.primitives;

import org.hzt.utils.iterators.State;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.LongPredicate;

public final class LongFilteringIterator implements PrimitiveIterator.OfLong {

    private final PrimitiveIterator.OfLong iterator;
    private final LongPredicate predicate;
    private final boolean sendWhen;
    private State nextState = State.NEXT_UNKNOWN;
    private long nextLong = 0L;

    private LongFilteringIterator(final OfLong iterator, final LongPredicate predicate, final boolean sendWhen) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.sendWhen = sendWhen;
    }

    public static LongFilteringIterator of(final OfLong iterator, final LongPredicate predicate, final boolean sendWhen) {
        return new LongFilteringIterator(iterator, predicate, sendWhen);
    }

    @Override
    public boolean hasNext() {
        if (nextState == State.NEXT_UNKNOWN) {
            calculateNext();
        }
        return nextState == State.CONTINUE;
    }

    @Override
    public long nextLong() {
        if (nextState == State.NEXT_UNKNOWN) {
            calculateNext();
        }
        if (nextState == State.DONE) {
            throw new NoSuchElementException();
        }
        final var result = nextLong;
        nextLong = 0L;
        nextState = State.NEXT_UNKNOWN;
        return result;
    }

    private void calculateNext() {
        while (iterator.hasNext()) {
            final var next = iterator.nextLong();
            if (predicate.test(next) == sendWhen) {
                this.nextLong = next;
                nextState = State.CONTINUE;
                return;
            }
        }
        nextState = State.DONE;
    }
}
