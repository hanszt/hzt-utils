package org.hzt.utils.iterators.primitives;

import java.util.PrimitiveIterator.OfDouble;
import java.util.function.DoublePredicate;

public final class DoubleSkipWhileIterator implements OfDouble {

    private final OfDouble iterator;
    private final DoublePredicate predicate;
    private final boolean inclusive;

    private double nextItem = 0.0;
    private boolean firstIteration = true;
    private SkipState state = SkipState.SKIPPING;

    DoubleSkipWhileIterator(final OfDouble iterator, final DoublePredicate predicate, final boolean inclusive) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.inclusive = inclusive;
    }

    public static OfDouble of(final OfDouble iterator, final DoublePredicate predicate, final boolean inclusive) {
        return new DoubleSkipWhileIterator(iterator, predicate, inclusive);
    }

    private void skip() {
        while (iterator.hasNext()) {
            nextItem = iterator.nextDouble();
            if (!predicate.test(nextItem)) {
                if (!firstIteration && inclusive && iterator.hasNext()) {
                    nextItem = iterator.nextDouble();
                }
                state = SkipState.NEXT_ITEM;
                firstIteration = false;
                return;
            }
            firstIteration = false;
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
            final double result = nextItem;
            state = SkipState.NORMAL_ITERATION;
            return result;
        }
        return iterator.nextDouble();
    }

    private enum SkipState {
        SKIPPING, NEXT_ITEM, NORMAL_ITERATION
    }
}
