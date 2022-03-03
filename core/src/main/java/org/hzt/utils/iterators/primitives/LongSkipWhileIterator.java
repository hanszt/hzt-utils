package org.hzt.utils.iterators.primitives;

import java.util.PrimitiveIterator.OfLong;
import java.util.function.LongPredicate;

public final class LongSkipWhileIterator implements OfLong {

    private final OfLong iterator;
    private final LongPredicate predicate;
    private final boolean inclusive;

    private long nextItem = 0;
    private boolean firstIteration = true;
    private SkipState state = SkipState.SKIPPING;

    LongSkipWhileIterator(OfLong iterator, LongPredicate predicate, boolean inclusive) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.inclusive = inclusive;
    }

    public static OfLong of(OfLong iterator, LongPredicate predicate, boolean inclusive) {
        return new LongSkipWhileIterator(iterator, predicate, inclusive);
    }

    private void skip() {
        while (iterator.hasNext()) {
            nextItem = iterator.nextLong();
            if (!predicate.test(nextItem)) {
                if (!firstIteration && inclusive && iterator.hasNext()) {
                    nextItem = iterator.nextLong();
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
    public long nextLong() {
        if (state == SkipState.SKIPPING) {
            skip();
        }
        if (state == SkipState.NEXT_ITEM) {
            long result = nextItem;
            state = SkipState.NORMAL_ITERATION;
            return result;
        }
        return iterator.nextLong();
    }

    private enum SkipState {
        SKIPPING, NEXT_ITEM, NORMAL_ITERATION
    }
}
