package org.hzt.utils.iterators.primitives;

import org.hzt.utils.iterators.State;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.IntPredicate;

public final class IntFilteringIterator implements PrimitiveIterator.OfInt {

    private final OfInt iterator;
    private final IntPredicate predicate;
    private final boolean sendWhen;
    private State nextState = State.NEXT_UNKNOWN;
    private int nextInt = 0;

    private IntFilteringIterator(final OfInt iterator, final IntPredicate predicate, final boolean sendWhen) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.sendWhen = sendWhen;
    }

    public static IntFilteringIterator of(final OfInt iterator, final IntPredicate predicate, final boolean sendWhen) {
        return new IntFilteringIterator(iterator, predicate, sendWhen);
    }

    @Override
    public boolean hasNext() {
        if (nextState == State.NEXT_UNKNOWN) {
            calculateNext();
        }
        return nextState == State.CONTINUE;
    }

    @Override
    public int nextInt() {
        if (nextState == State.NEXT_UNKNOWN) {
            calculateNext();
        }
        if (nextState == State.DONE) {
            throw new NoSuchElementException();
        }
        final int result = nextInt;
        nextInt = 0;
        nextState = State.NEXT_UNKNOWN;
        return result;
    }

    private void calculateNext() {
        while (iterator.hasNext()) {
            final int next = iterator.nextInt();
            if (predicate.test(next) == sendWhen) {
                this.nextInt = next;
                nextState = State.CONTINUE;
                return;
            }
        }
        nextState = State.DONE;
    }
}
