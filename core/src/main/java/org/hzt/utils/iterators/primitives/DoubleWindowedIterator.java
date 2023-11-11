package org.hzt.utils.iterators.primitives;

import org.hzt.utils.collections.primitives.DoubleList;
import org.hzt.utils.collections.primitives.DoubleMutableList;
import org.hzt.utils.iterators.AbstractIterator;

import java.util.PrimitiveIterator;
import java.util.function.IntUnaryOperator;

public final class DoubleWindowedIterator extends AbstractIterator<DoubleList> {

    private final PrimitiveIterator.OfDouble iterator;
    private final int initSize;
    private final IntUnaryOperator nextSizeSupplier;
    private final int initStep;
    private final IntUnaryOperator nextStepSupplier;
    private final boolean partialWindows;

    private int size = 0;
    private int step = 0;
    private DoubleMutableList nextWindow = DoubleMutableList.empty();

    private DoubleWindowedIterator(
            final PrimitiveIterator.OfDouble iterator,
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

    public static DoubleWindowedIterator of(final PrimitiveIterator.OfDouble iterator,
                                            final int initSize,
                                            final IntUnaryOperator nextSizeSupplier,
                                            final int initStep,
                                            final IntUnaryOperator nextStepSupplier,
                                            final boolean partialWindows) {
        return new DoubleWindowedIterator(iterator, initSize, nextSizeSupplier, initStep, nextStepSupplier, partialWindows);
    }

    private DoubleMutableList computeNextWindow() {
        final var windowInitCapacity = Math.min(size, 1024);
        final var gap = step - size;
        size = calculateNextSize(size);
        if (gap >= 0) {
            computeNextForWindowedSequenceNoOverlap(windowInitCapacity, gap);
        } else {
            computeNextForWindowedSequenceOverlapping(windowInitCapacity);
        }
        step = calculateNextStep(step);
        return nextWindow;
    }

    private int calculateNextSize(final int cur) {
        final var next = cur <= 0 ? initSize : nextSizeSupplier.applyAsInt(cur);
        return (next > 0) ? next : 1;
    }

    private int calculateNextStep(final int cur) {
        final var next = cur <= 0 ? initStep : nextStepSupplier.applyAsInt(cur);
        return (next > 0) ? next : 1;
    }

    private void computeNextForWindowedSequenceOverlapping(final int windowInitCapacity) {
        nextWindow = nextWindow.isEmpty() ? DoubleMutableList.withInitCapacity(windowInitCapacity) : DoubleMutableList.of(nextWindow);
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
            nextWindow.add(iterator.nextDouble());
        }
    }

    private void computeNextForWindowedSequenceNoOverlap(final int bufferInitCapacity, final int gap) {
        var skip = gap;
        nextWindow = DoubleMutableList.withInitCapacity(bufferInitCapacity);
        while (iterator.hasNext()) {
            final var item = iterator.nextDouble();
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
