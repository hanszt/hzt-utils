package org.hzt.utils.sequences.primitives;

import org.hzt.utils.It;
import org.hzt.utils.collections.primitives.DoubleList;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.DoubleWindowedIterator;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.SequenceHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.ToDoubleFunction;

@FunctionalInterface
public interface DoubleWindowedSequence extends PrimitiveIterable.OfDouble {

    default Sequence<DoubleList> chunked(int size) {
        return windowed(size, size, true);
    }

    default Sequence<DoubleList> chunked(IntSupplier nextSizeSupplier) {
        return chunked(nextSizeSupplier.getAsInt(), size -> nextSizeSupplier.getAsInt());
    }

    default Sequence<DoubleList> chunked(int initSize, IntUnaryOperator nextSizeSupplier) {
        //The holding consumer provides a way to provide the same value to both the size and step unary operator.
        //It also makes sure the nextSizeSupplier from this method is only called once instead of twice
        //It is required that the call for next size is made before next step for them to receive the same value
        final AtomicInteger holdingConsumer = new AtomicInteger();
        return windowed(initSize, size -> {
            final int nextSize = nextSizeSupplier.applyAsInt(size);
            holdingConsumer.set(nextSize);
            return nextSize;
        }, initSize, step -> holdingConsumer.get(), true);
    }

    default Sequence<DoubleList> windowed(int size, int step, boolean partialWindows) {
        return windowed(size, It::asInt, step, It::asInt, partialWindows);
    }

    default Sequence<DoubleList> windowed(int initSize,
                                          @NotNull IntUnaryOperator nextSizeSupplier,
                                          int initStep,
                                          @NotNull IntUnaryOperator nextStepSupplier,
                                          boolean partialWindows) {
        SequenceHelper.checkInitWindowSizeAndStep(initSize, initStep);
        return () -> DoubleWindowedIterator.of(iterator(), initSize, nextSizeSupplier, initStep, nextStepSupplier, partialWindows);
    }

    default Sequence<DoubleList> windowed(int initSize,
                                          @NotNull IntUnaryOperator nextSizeSupplier,
                                          int initStep,
                                          @NotNull IntUnaryOperator nextStepSupplier) {
        return windowed(initSize, nextSizeSupplier, initStep, nextStepSupplier, false);
    }

    default Sequence<DoubleList> windowed(int size, int step) {
        return windowed(size, step, false);
    }

    default Sequence<DoubleList> windowed(int size) {
        return windowed(size, 1);
    }

    default Sequence<DoubleList> windowed(int size, boolean partialWindows) {
        return windowed(size, 1, partialWindows);
    }

    default DoubleSequence windowed(int size, int step, boolean partialWindows,
                                    @NotNull ToDoubleFunction<DoubleList> reducer) {
        return windowed(size, step, partialWindows).mapToDouble(reducer);
    }

    default DoubleSequence windowed(int size, int step, @NotNull ToDoubleFunction<DoubleList> reducer) {
        return windowed(size, step, false, reducer);
    }

    default DoubleSequence windowed(int size, @NotNull ToDoubleFunction<DoubleList> reducer) {
        return windowed(size, 1, reducer);
    }

    default DoubleSequence windowed(int size, boolean partialWindows, @NotNull ToDoubleFunction<DoubleList> reducer) {
        return windowed(size, 1, partialWindows, reducer);
    }
}
