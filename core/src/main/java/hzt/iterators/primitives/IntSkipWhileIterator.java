package hzt.iterators.primitives;

import java.util.PrimitiveIterator.OfInt;
import java.util.function.IntPredicate;

public final class IntSkipWhileIterator implements OfInt {

    private final OfInt iterator;
    private final IntPredicate predicate;
    private final boolean inclusive;

    private boolean inclusiveSkipped = false;
    private int nextItem = 0;
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
        if (inclusive && inclusiveSkipped && iterator.hasNext()) {
            nextItem = iterator.nextInt();
            state = SkipState.NEXT_ITEM;
            return;
        }
        while (iterator.hasNext()) {
            final int item = iterator.nextInt();
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
    public int nextInt() {
        if (state == SkipState.SKIPPING) {
            skip();
        }
        if (state == SkipState.NEXT_ITEM) {
            int result = nextItem;
            state = SkipState.NORMAL_ITERATION;
            return result;
        }
        return iterator.nextInt();
    }

    private enum SkipState {
        SKIPPING, NEXT_ITEM, NORMAL_ITERATION
    }
}
