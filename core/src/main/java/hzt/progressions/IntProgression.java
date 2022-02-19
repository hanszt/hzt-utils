package hzt.progressions;

import hzt.sequences.primitives.IntSequence;

import java.util.PrimitiveIterator;

public class IntProgression implements IntSequence {

    private final int start;
    private final int endInclusive;
    private final int step;

    private int next;

    public IntProgression(int start, int endInclusive, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("step must be none-zero");
        }
        if (step == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Step must be greater than Int.MIN_VALUE to avoid overflow on negation.");
        }
        this.start = start;
        this.endInclusive = endInclusive;
        this.step = step;
    }

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return new PrimitiveIterator.OfInt() {
            @Override
            public int nextInt() {
                final int value = next;
                next += step;
                return value;
            }

            @Override
            public boolean hasNext() {
                return (step > 0) ? (next <= endInclusive) : (next >= endInclusive);
            }
        };
    }

    protected int getStart() {
        return start;
    }

    protected int getEndInclusive() {
        return endInclusive;
    }
}
