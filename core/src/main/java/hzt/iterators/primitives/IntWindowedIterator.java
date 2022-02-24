package hzt.iterators.primitives;

import hzt.collections.primitives.IntListX;
import hzt.collections.primitives.IntMutableListX;
import hzt.iterators.AbstractIterator;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;

public final class IntWindowedIterator extends AbstractIterator<IntListX> {

    private final PrimitiveIterator.OfInt iterator;
    private final int size;
    private final int step;
    private final boolean partialWindows;

    private IntMutableListX nextWindow = IntMutableListX.empty();
    private int skip = 0;

    private IntWindowedIterator(
            @NotNull PrimitiveIterator.OfInt iterator,
            int size,
            int step,
            boolean partialWindows) {
        this.iterator = iterator;
        this.size = size;
        this.step = step;
        this.partialWindows = partialWindows;
    }

    public static IntWindowedIterator of(@NotNull PrimitiveIterator.OfInt iterator,
                                         int size,
                                         int step,
                                         boolean partialWindows) {
        return new IntWindowedIterator(iterator, size, step, partialWindows);
    }

    private IntMutableListX computeNextWindow() {
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
        nextWindow = nextWindow.isEmpty() ? IntMutableListX.withInitCapacity(windowInitCapacity) : IntMutableListX.of(nextWindow);
        if (nextWindow.isEmpty()) {
            fillIfWindowEmpty();
        } else {
            calculateNextWindow();
        }
        if (!partialWindows && nextWindow.size() < size) {
            nextWindow = IntMutableListX.empty();
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
                nextWindow.removeAt(0);
            }
            if (iterator.hasNext()) {
                nextWindow.add(iterator.next());
            }
            stepCount--;
        }
    }

    private void computeNextForWindowedSequenceNoOverlap(int bufferInitCapacity, int gap) {
        nextWindow = IntMutableListX.withInitCapacity(bufferInitCapacity);
        while (iterator.hasNext()) {
            int item = iterator.nextInt();
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
            setNext(next);
        }
    }
}
