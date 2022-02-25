package hzt.iterators.primitives;

import hzt.collections.primitives.LongListX;
import hzt.collections.primitives.LongMutableListX;
import hzt.iterators.AbstractIterator;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;

public final class LongWindowedIterator extends AbstractIterator<LongListX> {

    private final PrimitiveIterator.OfLong iterator;
    private final int size;
    private final int step;
    private final boolean partialWindows;

    private LongMutableListX nextWindow = LongMutableListX.empty();
    private int skip = 0;

    private LongWindowedIterator(
            @NotNull PrimitiveIterator.OfLong iterator,
            int size,
            int step,
            boolean partialWindows) {
        this.iterator = iterator;
        this.size = size;
        this.step = step;
        this.partialWindows = partialWindows;
    }

    public static LongWindowedIterator of(@NotNull PrimitiveIterator.OfLong iterator,
                                              int size,
                                              int step,
                                              boolean partialWindows) {
        return new LongWindowedIterator(iterator, size, step, partialWindows);
    }

    private LongMutableListX computeNextWindow() {
        int windowInitCapacity = Math.min(size, 1024);
        final int gap = step - size;
        if (gap >= 0) {
            computeNextForWindowedSequenceNoOverlap(windowInitCapacity, gap);
        } else {
            computeNextForWindowedSequenceOverlapping(windowInitCapacity);
        }
        return nextWindow;
    }

    private void computeNextForWindowedSequenceOverlapping(int windowInitCapacity) {
        nextWindow = nextWindow.isEmpty() ? LongMutableListX.withInitCapacity(windowInitCapacity) : LongMutableListX.of(nextWindow);
        if (nextWindow.isEmpty()) {
            fillIfWindowEmpty();
        } else {
            calculateNextWindow();
        }
        if (!partialWindows && nextWindow.size() < size) {
            nextWindow = LongMutableListX.empty();
        }
    }

    private void fillIfWindowEmpty() {
        while (iterator.hasNext() && nextWindow.size() < size) {
            nextWindow.add(iterator.nextLong());
        }
    }

    private void calculateNextWindow() {
        int stepCount = step;
        while (stepCount > 0) {
            if (!nextWindow.isEmpty()) {
                nextWindow.removeAt(0);
            }
            if (iterator.hasNext()) {
                nextWindow.add(iterator.nextLong());
            }
            stepCount--;
        }
    }

    private void computeNextForWindowedSequenceNoOverlap(int bufferInitCapacity, int gap) {
        nextWindow = LongMutableListX.withInitCapacity(bufferInitCapacity);
        while (iterator.hasNext()) {
            long item = iterator.nextLong();
            if (skip > 0) {
                skip -= 1;
                continue;
            }
            nextWindow.add(item);
            if (nextWindow.size() == size) {
                skip = gap;
                return;
            }
        }
        if (!nextWindow.isEmpty() && !partialWindows) {
            nextWindow.clear();
        }
    }

    @Override
    protected void computeNext() {
        final LongMutableListX next = computeNextWindow();
        if (next.isEmpty()) {
            done();
        } else {
            setNext(next);
        }
    }
}
