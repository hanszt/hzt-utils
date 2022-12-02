package org.hzt.fx.utils.iterators;

import org.hzt.fx.utils.function.FloatPredicate;

final class FloatSkipWhileIterator implements FloatIterator {

    private final FloatIterator iterator;
    private final FloatPredicate predicate;
    private final boolean inclusive;

    private float nextItem = 0F;
    private boolean firstIteration = true;
    private SkipState state = SkipState.SKIPPING;

    FloatSkipWhileIterator(FloatIterator iterator, FloatPredicate predicate, boolean inclusive) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.inclusive = inclusive;
    }

    public static FloatIterator of(FloatIterator iterator, FloatPredicate predicate, boolean inclusive) {
        return new FloatSkipWhileIterator(iterator, predicate, inclusive);
    }

    private void skip() {
        while (iterator.hasNext()) {
            nextItem = iterator.nextFloat();
            if (!predicate.test(nextItem)) {
                if (!firstIteration && inclusive && iterator.hasNext()) {
                    nextItem = iterator.nextFloat();
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
    public float nextFloat() {
        if (state == SkipState.SKIPPING) {
            skip();
        }
        if (state == SkipState.NEXT_ITEM) {
            var result = nextItem;
            state = SkipState.NORMAL_ITERATION;
            return result;
        }
        return iterator.nextFloat();
    }

    private enum SkipState {
        SKIPPING, NEXT_ITEM, NORMAL_ITERATION
    }
}
