package hzt.iterators;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.IntPredicate;

public final class IntTakeWhileIterator implements PrimitiveIterator.OfInt {

    private final OfInt iterator;
    private final IntPredicate predicate;
    private final boolean inclusive;

    private boolean inclusiveConsumed = false;
    private int nextLong;
    private State nextState = State.INIT_UNKNOWN;

    private IntTakeWhileIterator(OfInt iterator, IntPredicate predicate, boolean inclusive) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.inclusive = inclusive;
    }

    public static IntTakeWhileIterator of(OfInt iterator, IntPredicate predicate, boolean inclusive) {
        return new IntTakeWhileIterator(iterator, predicate, inclusive);
    }

    public static IntTakeWhileIterator of(OfInt iterator, IntPredicate predicate) {
        return new IntTakeWhileIterator(iterator, predicate, false);
    }

    private void calculateNext() {
        if (iterator.hasNext()) {
            final int item = iterator.nextInt();
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
    public int nextInt() {
        if (nextState.isUnknown()) {
            calculateNext();
        }
        if (nextState == State.DONE) {
            throw new NoSuchElementException();
        }
        int result = nextLong;
        nextState = State.NEXT_UNKNOWN;
        return result;
    }
}
