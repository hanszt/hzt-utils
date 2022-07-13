package org.hzt.utils.iterators.primitives;

import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.IntMutableList;
import org.hzt.utils.iterators.AbstractIterator;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.IntUnaryOperator;

public final class IntWindowedIterator extends AbstractIterator<IntList> {

    private final PrimitiveIterator.OfInt iterator;
    private final int initSize;
    private final IntUnaryOperator nextSizeSupplier;
    private final int initStep;
    private final IntUnaryOperator nextStepSupplier;
    private final boolean partialWindows;

    private int size = 0;
    private int step = 0;
    private IntMutableList nextWindow = IntMutableList.empty();

    private IntWindowedIterator(
            @NotNull PrimitiveIterator.OfInt iterator,
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

    public static IntWindowedIterator of(@NotNull PrimitiveIterator.OfInt iterator,
                                         int initSize,
                                         @NotNull IntUnaryOperator nextSizeSupplier,
                                         int initStep,
                                         @NotNull IntUnaryOperator nextStepSupplier,
                                         boolean partialWindows) {
        return new IntWindowedIterator(iterator, initSize, nextSizeSupplier, initStep, nextStepSupplier, partialWindows);
    }

    private IntMutableList computeNextWindow() {
        int windowInitCapacity = Math.min(size, 1024);
        final int gap = step - size;
        size = calculateNextSize(size);
        if (gap >= 0) {
            computeNextForWindowedSequenceNoOverlap(windowInitCapacity, gap);
        } else {
            computeNextForWindowedSequenceOverlapping(windowInitCapacity);
        }
        step = calculateNextStep(step);
        return nextWindow;
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
        nextWindow = nextWindow.isEmpty() ? IntMutableList.withInitCapacity(windowInitCapacity) : IntMutableList.of(nextWindow);
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
            nextWindow.add(iterator.nextInt());
        }
    }

    private void computeNextForWindowedSequenceNoOverlap(int bufferInitCapacity, int gap) {
        int skip = gap;
        nextWindow = IntMutableList.withInitCapacity(bufferInitCapacity);
        while (iterator.hasNext()) {
            int item = iterator.nextInt();
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
        final IntMutableList next = computeNextWindow();
        if (next.isNotEmpty()) {
            setNext(next);
        } else {
            done();
        }
    }
}
