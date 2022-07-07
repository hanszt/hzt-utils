package org.hzt.utils.iterators;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MutableListX;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.IntUnaryOperator;

public final class WindowedIterator<T> extends AbstractIterator<ListX<T>> {

    private final Iterator<T> iterator;
    private final int initSize;
    private final IntUnaryOperator nextSizeSupplier;
    private final int initStep;
    private final IntUnaryOperator nextStepSupplier;
    private final boolean partialWindows;

    private int step = 0;
    private int size = 0;
    private MutableListX<T> nextWindow = MutableListX.empty();

    private WindowedIterator(
            @NotNull Iterator<T> iterator,
            int initSize,
            @NotNull IntUnaryOperator nextSizeSupplier,
            int initStep,
            @NotNull IntUnaryOperator nextStepSupplier,
            boolean partialWindows) {
        this.iterator = iterator;
        this.initSize = initSize;
        this.nextSizeSupplier = nextSizeSupplier;
        this.initStep = initStep;
        this.nextStepSupplier = nextStepSupplier;
        this.partialWindows = partialWindows;
    }

    public static <T> WindowedIterator<T> of(@NotNull Iterator<T> iterator,
                                             int initSize,
                                             @NotNull IntUnaryOperator nextSizeSupplier,
                                             int initStep,
                                             @NotNull IntUnaryOperator nextStepSupplier,
                                             boolean partialWindows) {
        return new WindowedIterator<>(iterator, initSize, nextSizeSupplier, initStep, nextStepSupplier, partialWindows);
    }

    private ListX<T> computeNextWindow() {
        int windowInitCapacity = Math.min(size, 1024);
        final int gap = step - size;
        size = calculateNextSize(size);
        if (gap >= 0) {
            computeNextForWindowedSequenceNoOverlap(windowInitCapacity, gap);
        } else {
            computeNextForWindowedSequenceOverlapping(windowInitCapacity);
        }
        step = calculateNextStep(step);
        return ListX.copyOf(nextWindow);
    }

    private int calculateNextSize(int cur) {
        int next = cur <= 0 ? initSize : nextSizeSupplier.applyAsInt(cur);
        return (next > 0) ? next : 1;
    }

    private int calculateNextStep(int cur) {
        int next = cur <= 0 ? initStep : nextStepSupplier.applyAsInt(cur);
        return (next > 0) ? next : 1;
    }

    private void computeNextForWindowedSequenceOverlapping(int windowInitCapacity) {
        nextWindow = nextWindow.isEmpty() ? MutableListX.withInitCapacity(windowInitCapacity) : MutableListX.of(nextWindow);
        calculateNextOverlappingWindow();
        if (!partialWindows && nextWindow.size() < size) {
            nextWindow.clear();
        }
    }

    private void calculateNextOverlappingWindow() {
        int stepCount = 0;
        while (stepCount < step && nextWindow.isNotEmpty()) {
            nextWindow.removeFirst();
            stepCount++;
        }
        while (iterator.hasNext() && nextWindow.size() < size) {
            nextWindow.add(iterator.next());
        }
    }

    private void computeNextForWindowedSequenceNoOverlap(int bufferInitCapacity, int gap) {
        int skip = gap;
        nextWindow = MutableListX.withInitCapacity(bufferInitCapacity);
        while (iterator.hasNext()) {
            T item = iterator.next();
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
        final ListX<T> next = computeNextWindow();
        if (next.isNotEmpty()) {
            setNext(next);
        } else {
            done();
        }
    }
}
