package hzt.iterators;

import hzt.sequences.primitives.LongSequence;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;

public final class LongWindowedIterator extends AbstractIterator<LongSequence> {

    private final PrimitiveIterator.OfLong iterator;
    private final int size;
    private final int step;
    private final boolean partialWindows;

    private LongBuffer nextWindow = new LongBuffer();
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

    private LongBuffer computeNextWindow() {
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
        nextWindow = nextWindow.isEmpty() ? new LongBuffer(windowInitCapacity) : new LongBuffer(nextWindow);
        if (nextWindow.isEmpty()) {
            fillIfWindowEmpty();
        } else {
            calculateNextWindow();
        }
        if (!partialWindows && nextWindow.size() < size) {
            nextWindow = new LongBuffer();
        }
    }

    private void fillIfWindowEmpty() {
        while (iterator.hasNext() && nextWindow.size() < size) {
            nextWindow.add(iterator.next());
        }
    }

    private void calculateNextWindow() {
        int stepCount = step;
        while (stepCount > 0) {
            if (!nextWindow.isEmpty()) {
                nextWindow.remove(0);
            }
            if (iterator.hasNext()) {
                nextWindow.add(iterator.next());
            }
            stepCount--;
        }
    }

    private void computeNextForWindowedSequenceNoOverlap(int bufferInitCapacity, int gap) {
        nextWindow = new LongBuffer(bufferInitCapacity);
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
        final var next = computeNextWindow();
        if (next.isEmpty()) {
            done();
        } else {
            setNext(LongSequence.of(next));
        }
    }
}
