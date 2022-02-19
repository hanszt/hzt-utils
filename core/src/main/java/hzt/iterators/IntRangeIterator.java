package hzt.iterators;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public final class IntRangeIterator implements PrimitiveIterator.OfInt {

    private final int start;
    private final int endInclusive;
    private final int step;

    private int nextInt;

    private IntRangeIterator(int start, int endInclusive, int step) {
        this.start = start;
        this.endInclusive = endInclusive;
        this.step = step;
        this.nextInt = start;
    }

    public static OfInt of(int start, int endInclusive, int step) {
        return new IntRangeIterator(start, endInclusive, step);
    }

    @Override
    public int nextInt() {
        if (checkHasNext()) {
            int current = nextInt;
            nextInt = nextInt + (start < endInclusive ? step : -step);
            return current;
        }
        throw new NoSuchElementException(nextInt + " is outside defined range");
    }

    @Override
    public boolean hasNext() {
        return checkHasNext();
    }

    private boolean checkHasNext() {
        if (start <= endInclusive) {
            return nextInt <= endInclusive;
        }
        return nextInt >= endInclusive;
    }
}
