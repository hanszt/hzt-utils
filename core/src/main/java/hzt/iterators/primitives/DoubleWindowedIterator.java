package hzt.iterators.primitives;

import hzt.iterators.AbstractIterator;
import hzt.sequences.primitives.DoubleSequence;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;

public final class DoubleWindowedIterator extends AbstractIterator<DoubleSequence> {

    private final PrimitiveIterator.OfDouble iterator;
    private final int size;
    private final int step;
    private final boolean partialWindows;

    private DoubleBuffer nextWindow = new DoubleBuffer();
    private int skip = 0;

    private DoubleWindowedIterator(
            @NotNull PrimitiveIterator.OfDouble iterator,
            int size,
            int step,
            boolean partialWindows) {
        this.iterator = iterator;
        this.size = size;
        this.step = step;
        this.partialWindows = partialWindows;
    }

    public static DoubleWindowedIterator of(@NotNull PrimitiveIterator.OfDouble iterator,
                                            int size,
                                            int step,
                                            boolean partialWindows) {
        return new DoubleWindowedIterator(iterator, size, step, partialWindows);
    }

    private DoubleBuffer computeNextWindow() {
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
        nextWindow = nextWindow.isEmpty() ? new DoubleBuffer(windowInitCapacity) : new DoubleBuffer(nextWindow);
        if (nextWindow.isEmpty()) {
            fillIfWindowEmpty();
        } else {
            calculateNextWindow();
        }
        if (!partialWindows && nextWindow.size() < size) {
            nextWindow = new DoubleBuffer();
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
        nextWindow = new DoubleBuffer(bufferInitCapacity);
        while (iterator.hasNext()) {
            double item = iterator.nextDouble();
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
            setNext(DoubleSequence.of(next));
        }
    }
}
