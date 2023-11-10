package org.hzt.utils.sequences.primitives;

import org.hzt.utils.It;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.IntWindowedIterator;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.SequenceHelper;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;

@FunctionalInterface
public interface IntWindowedSequence extends PrimitiveIterable.OfInt {

    default Sequence<IntList> chunked(final int size) {
        return windowed(size, size, true);
    }

    default Sequence<IntList> chunked(final IntSupplier nextSizeSupplier) {
        return chunked(nextSizeSupplier.getAsInt(), size -> nextSizeSupplier.getAsInt());
    }

    default Sequence<IntList> chunked(final int initSize, final IntUnaryOperator nextSizeSupplier) {
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

    default Sequence<IntList> windowed(final int size, final int step, final boolean partialWindows) {
        return windowed(size, It::asInt, step, It::asInt, partialWindows);
    }

    default Sequence<IntList> windowed(final int initSize,
                                       final IntUnaryOperator nextSizeSupplier,
                                       final int initStep,
                                       final IntUnaryOperator nextStepSupplier,
                                       final boolean partialWindows) {
        SequenceHelper.checkInitWindowSizeAndStep(initSize, initStep);
        return () -> IntWindowedIterator.of(iterator(), initSize, nextSizeSupplier, initStep, nextStepSupplier, partialWindows);
    }

    default Sequence<IntList> windowed(final int size, final int step) {
        return windowed(size, step, false);
    }

    default Sequence<IntList> windowed(final int size) {
        return windowed(size, 1);
    }

    default Sequence<IntList> windowed(final int size, final boolean partialWindows) {
        return windowed(size, 1, partialWindows);
    }

    default IntSequence windowed(final int size, final int step, final boolean partialWindows,
                                 final ToIntFunction<IntList> reducer) {
        return windowed(size, step, partialWindows).mapToInt(reducer);
    }

    default IntSequence windowed(final int size, final int step, final ToIntFunction<IntList> reducer) {
        return windowed(size, step, false, reducer);
    }

    default IntSequence windowed(final int size, final ToIntFunction<IntList> reducer) {
        return windowed(size, 1, reducer);
    }

    default IntSequence windowed(final int size, final boolean partialWindows, final ToIntFunction<IntList> reducer) {
        return windowed(size, 1, partialWindows, reducer);
    }
}
