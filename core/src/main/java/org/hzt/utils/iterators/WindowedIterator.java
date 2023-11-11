package org.hzt.utils.iterators;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MutableListX;

import java.util.Iterator;
import java.util.function.IntUnaryOperator;

final class WindowedIterator<T> extends AbstractIterator<ListX<T>> {

    private final Iterator<T> iterator;
    private final int initSize;
    private final IntUnaryOperator nextSizeSupplier;
    private final int initStep;
    private final IntUnaryOperator nextStepSupplier;
    private final boolean partialWindows;

    private int step = 0;
    private int size = 0;
    private MutableListX<T> nextWindow = MutableListX.empty();

    WindowedIterator(
            final Iterator<T> iterator,
            final int initSize,
            final IntUnaryOperator nextSizeSupplier,
            final int initStep,
            final IntUnaryOperator nextStepSupplier,
            final boolean partialWindows) {
        this.iterator = iterator;
        this.initSize = initSize;
        this.nextSizeSupplier = nextSizeSupplier;
        this.initStep = initStep;
        this.nextStepSupplier = nextStepSupplier;
        this.partialWindows = partialWindows;
    }

    private ListX<T> computeNextWindow() {
        final var windowInitCapacity = Math.min(size, 1024);
        final var gap = step - size;
        size = calculateNextSize(size);
        if (gap >= 0) {
            computeNextNoOverlap(windowInitCapacity, gap);
        } else {
            computeNextOverlapping(windowInitCapacity);
        }
        step = calculateNextStep(step);
        return ListX.copyOf(nextWindow);
    }

    private int calculateNextSize(final int cur) {
        final var next = cur <= 0 ? initSize : nextSizeSupplier.applyAsInt(cur);
        return (next > 0) ? next : 1;
    }

    private int calculateNextStep(final int cur) {
        final var next = cur <= 0 ? initStep : nextStepSupplier.applyAsInt(cur);
        return (next > 0) ? next : 1;
    }

    private void computeNextOverlapping(final int windowInitCapacity) {
        nextWindow = nextWindow.isEmpty() ? MutableListX.withInitCapacity(windowInitCapacity) : MutableListX.of(nextWindow);
        calculateNextOverlappingWindow();
        if (!partialWindows && nextWindow.size() < size) {
            nextWindow.clear();
        }
    }

    private void calculateNextOverlappingWindow() {
        var stepCount = 0;
        while (stepCount < step && nextWindow.isNotEmpty()) {
            nextWindow.removeFirst();
            stepCount++;
        }
        while (iterator.hasNext() && nextWindow.size() < size) {
            nextWindow.add(iterator.next());
        }
    }

    private void computeNextNoOverlap(final int bufferInitCapacity, final int gap) {
        var skip = gap;
        nextWindow = MutableListX.withInitCapacity(bufferInitCapacity);
        while (iterator.hasNext()) {
            final var item = iterator.next();
            if (skip > 0) {
                skip--;
                continue;
            }
            nextWindow.add(item);
            if (nextWindow.size() == size) {
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
        if (next.isNotEmpty()) {
            setNext(next);
        } else {
            done();
        }
    }
}
