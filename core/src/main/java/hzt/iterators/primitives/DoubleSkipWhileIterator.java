package hzt.iterators.primitives;

import java.util.PrimitiveIterator.OfDouble;
import java.util.function.DoublePredicate;

public final class DoubleSkipWhileIterator implements OfDouble {

    private final OfDouble iterator;
    private final DoublePredicate predicate;
    private final boolean inclusive;

    private boolean inclusiveSkipped = false;
    private double nextItem = 0L;
    private SkipState state = SkipState.SKIPPING;

    DoubleSkipWhileIterator(OfDouble iterator, DoublePredicate predicate, boolean inclusive) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.inclusive = inclusive;
    }

    public static OfDouble of(OfDouble iterator, DoublePredicate predicate, boolean inclusive) {
        return new DoubleSkipWhileIterator(iterator, predicate, inclusive);
    }

    private void skip() {
        if (inclusive && inclusiveSkipped && iterator.hasNext()) {
            nextItem = iterator.next();
            state = SkipState.NEXT_ITEM;
            return;
        }
        while (iterator.hasNext()) {
            final double item = iterator.next();
            if (!predicate.test(item)) {
                if (inclusive && !inclusiveSkipped) {
                    inclusiveSkipped = true;
                    return;
                }
                nextItem = item;
                state = SkipState.NEXT_ITEM;
                return;
            }
        }
        state = SkipState.NORMAL_ITERATION;
    }

    @Override
    public boolean hasNext() {
        if (state == SkipState.SKIPPING) {
            skip();
        }
        return state == SkipState.NEXT_ITEM || iterator.hasNext();
    }

    @Override
    public double nextDouble() {
        if (state == SkipState.SKIPPING) {
            skip();
        }
        if (state == SkipState.NEXT_ITEM) {
            double result = nextItem;
            state = SkipState.NORMAL_ITERATION;
            return result;
        }
        return iterator.next();
    }

    private enum SkipState {
        SKIPPING, NEXT_ITEM, NORMAL_ITERATION
    }
}
