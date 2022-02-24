package hzt.iterators.primitives;

import java.util.PrimitiveIterator.OfLong;
import java.util.function.LongPredicate;

public final class LongSkipWhileIterator implements OfLong {

    private final OfLong iterator;
    private final LongPredicate predicate;
    private final boolean inclusive;

    private boolean inclusiveSkipped = false;
    private long nextItem = 0L;
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
        if (inclusive && inclusiveSkipped && iterator.hasNext()) {
            nextItem = iterator.next();
            state = SkipState.NEXT_ITEM;
            return;
        }
        while (iterator.hasNext()) {
            final long item = iterator.next();
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
    public long nextLong() {
        if (state == SkipState.SKIPPING) {
            skip();
        }
        if (state == SkipState.NEXT_ITEM) {
            long result = nextItem;
            state = SkipState.NORMAL_ITERATION;
            return result;
        }
        return iterator.next();
    }

    private enum SkipState {
        SKIPPING, NEXT_ITEM, NORMAL_ITERATION
    }
}
