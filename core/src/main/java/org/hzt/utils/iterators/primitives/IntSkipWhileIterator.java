package org.hzt.utils.iterators.primitives;

import java.util.PrimitiveIterator.OfInt;
import java.util.function.IntPredicate;

public final class IntSkipWhileIterator implements OfInt {

    private final OfInt iterator;
    private final IntPredicate predicate;
    private final boolean inclusive;

    private int nextItem = 0;
    private boolean firstIteration = true;
    private SkipState state = SkipState.SKIPPING;

    IntSkipWhileIterator(OfInt iterator, IntPredicate predicate, boolean inclusive) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.inclusive = inclusive;
    }

    public static OfInt of(OfInt iterator, IntPredicate predicate, boolean inclusive) {
        return new IntSkipWhileIterator(iterator, predicate, inclusive);
    }

    private void skip() {
        while (iterator.hasNext()) {
            nextItem = iterator.nextInt();
            if (!predicate.test(nextItem)) {
                if (!firstIteration && inclusive && iterator.hasNext()) {
                    nextItem = iterator.nextInt();
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
    public int nextInt() {
        if (state == SkipState.SKIPPING) {
            skip();
        }
        if (state == SkipState.NEXT_ITEM) {
            var result = nextItem;
            state = SkipState.NORMAL_ITERATION;
            return result;
        }
        return iterator.nextInt();
    }

    private enum SkipState {
        SKIPPING, NEXT_ITEM, NORMAL_ITERATION
    }
}
