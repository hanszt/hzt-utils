package hzt.iterators;

import hzt.collections.ListX;
import hzt.collections.MutableListX;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class WindowedIterator<T> extends AbstractIterator<ListX<T>> {

    private final Iterator<T> iterator;
    private final int size;
    private final int step;
    private final boolean partialWindows;

    private MutableListX<T> nextWindow = MutableListX.empty();
    private int skip = 0;

    private WindowedIterator(
            @NotNull Iterator<T> iterator,
            int size,
            int step,
            boolean partialWindows) {
        this.iterator = iterator;
        this.size = size;
        this.step = step;
        this.partialWindows = partialWindows;
    }

    public static <T> WindowedIterator<T> of(@NotNull Iterator<T> iterator,
                                             int size,
                                             int step,
                                             boolean partialWindows) {
        return new WindowedIterator<>(iterator, size, step, partialWindows);
    }

    private ListX<T> computeNextWindow() {
        int windowInitCapacity = Math.min(size, 1024);
        final int gap = step - size;
        if (gap >= 0) {
            computeNextForWindowedSequenceNoOverlap(windowInitCapacity, gap);
        } else {
            computeNextForWindowedSequenceOverlapping(windowInitCapacity);
        }
        return ListX.of(nextWindow);
    }

    private void computeNextForWindowedSequenceOverlapping(int windowInitCapacity) {
        nextWindow = nextWindow.isEmpty() ? MutableListX.withInitCapacity(windowInitCapacity) : MutableListX.of(nextWindow);
        if (nextWindow.isEmpty()) {
            fillIfWindowEmpty();
        } else {
            calculateNextWindow();
        }
        if (!partialWindows && nextWindow.size() < size) {
            nextWindow.clear();
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
        nextWindow = MutableListX.withInitCapacity(bufferInitCapacity);
        while (iterator.hasNext()) {
            T item = iterator.next();
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
