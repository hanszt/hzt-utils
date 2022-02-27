package hzt.iterators.primitives;

import hzt.collections.primitives.IntListX;
import hzt.collections.primitives.IntMutableListX;
import hzt.iterators.AbstractIterator;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.IntUnaryOperator;

public final class IntWindowedIterator extends AbstractIterator<IntListX> {

    private final PrimitiveIterator.OfInt iterator;
    private final int initSize;
    private final IntUnaryOperator nextSizeSupplier;
    private final int initStep;
    private final IntUnaryOperator nextStepSupplier;
    private final boolean partialWindows;

    private int size = 0;
    private int step = 0;
    private IntMutableListX nextWindow = IntMutableListX.empty();

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

    private IntMutableListX computeNextWindow() {
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
        nextWindow = nextWindow.isEmpty() ? IntMutableListX.withInitCapacity(windowInitCapacity) : IntMutableListX.of(nextWindow);
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
            nextWindow.add(iterator.nextInt());
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
        nextWindow = IntMutableListX.withInitCapacity(bufferInitCapacity);
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
        final IntMutableListX next = computeNextWindow();
        if (next.isEmpty()) {
            done();
        } else {
            setNext(next);
        }
    }
}
