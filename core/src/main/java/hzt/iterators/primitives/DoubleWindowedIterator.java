package hzt.iterators.primitives;

import hzt.collections.primitives.DoubleListX;
import hzt.collections.primitives.DoubleMutableListX;
import hzt.iterators.AbstractIterator;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.IntUnaryOperator;

public final class DoubleWindowedIterator extends AbstractIterator<DoubleListX> {

    private final PrimitiveIterator.OfDouble iterator;
    private final int initSize;
    private final IntUnaryOperator nextSizeSupplier;
    private final int initStep;
    private final IntUnaryOperator nextStepSupplier;
    private final boolean partialWindows;

    private int size = 0;
    private int step = 0;
    private DoubleMutableListX nextWindow = DoubleMutableListX.empty();

    private DoubleWindowedIterator(
            @NotNull PrimitiveIterator.OfDouble iterator,
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

    public static DoubleWindowedIterator of(@NotNull PrimitiveIterator.OfDouble iterator,
                                            int initSize,
                                            @NotNull IntUnaryOperator nextSizeSupplier,
                                            int initStep,
                                            @NotNull IntUnaryOperator nextStepSupplier,
                                            boolean partialWindows) {
        return new DoubleWindowedIterator(iterator, initSize, nextSizeSupplier, initStep, nextStepSupplier, partialWindows);
    }

    private DoubleMutableListX computeNextWindow() {
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
        nextWindow = nextWindow.isEmpty() ? DoubleMutableListX.withInitCapacity(windowInitCapacity) : DoubleMutableListX.of(nextWindow);
        if (nextWindow.isEmpty()) {
            fillIfWindowEmpty();
        } else {
            calculateNextOverlappingWindowIfNotEmpty();
        }
        if (!partialWindows && nextWindow.size() < size) {
            nextWindow.clear();
        }
    }

    private void fillIfWindowEmpty() {
        while (iterator.hasNext() && nextWindow.size() < size) {
            nextWindow.add(iterator.nextDouble());
        }
    }

    private void calculateNextOverlappingWindowIfNotEmpty() {
        int stepCount = 0;
        while (stepCount < step && !nextWindow.isEmpty()) {
            nextWindow.removeFirst();
            stepCount++;
        }
        while (iterator.hasNext() && nextWindow.size() < size) {
            nextWindow.add(iterator.next());
        }
    }

    private void computeNextForWindowedSequenceNoOverlap(int bufferInitCapacity, int gap) {
        int skip = gap;
        nextWindow = DoubleMutableListX.withInitCapacity(bufferInitCapacity);
        while (iterator.hasNext()) {
            double item = iterator.nextDouble();
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
        final DoubleMutableListX next = computeNextWindow();
        if (next.isEmpty()) {
            done();
        } else {
            setNext(next);
        }
    }
}
