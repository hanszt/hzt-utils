package org.hzt.utils.sequences.primitives;

import org.hzt.utils.collections.primitives.LongListX;
import org.hzt.utils.iterables.primitives.LongIterable;
import org.hzt.utils.iterators.primitives.LongWindowedIterator;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.SequenceHelper;
import org.hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.ToLongFunction;

public interface LongWindowedSequence extends LongIterable {

    default Sequence<LongListX> chunked(int size) {
        return windowed(size, size, true);
    }

    default Sequence<LongListX> chunked(IntSupplier nextSizeSupplier) {
        return chunked(nextSizeSupplier.getAsInt(), size -> nextSizeSupplier.getAsInt());
    }

    default Sequence<LongListX> chunked(int initSize, IntUnaryOperator nextSizeSupplier) {
        //The holding consumer provides a way to provide the same value to both the size and step unary operator.
        //It also makes sure the nextSizeSupplier from this method is only called once instead of twice
        //It is required that the call for next size is made before next step for them to receive the same value
        final var holdingConsumer = new PrimitiveSequenceHelper.HoldingConsumer();
        return windowed(initSize, size -> {
            final var nextSize = nextSizeSupplier.applyAsInt(size);
            holdingConsumer.accept(nextSize);
            return nextSize;
        }, initSize, step -> holdingConsumer.getValue(), true);
    }

    default Sequence<LongListX> windowed(int size, int step, boolean partialWindows) {
        return windowed(size, It::asInt, step, It::asInt, partialWindows);
    }

    default Sequence<LongListX> windowed(int initSize,
                                        @NotNull IntUnaryOperator nextSizeSupplier,
                                        int initStep,
                                        @NotNull IntUnaryOperator nextStepSupplier,
                                        boolean partialWindows) {
        SequenceHelper.checkInitWindowSizeAndStep(initSize, initStep);
        return () -> LongWindowedIterator.of(iterator(), initSize, nextSizeSupplier, initStep, nextStepSupplier, partialWindows);
    }

    default Sequence<LongListX> windowed(int size, int step) {
        return windowed(size, step, false);
    }

    default Sequence<LongListX> windowed(int size) {
        return windowed(size, 1);
    }

    default Sequence<LongListX> windowed(int size, boolean partialWindows) {
        return windowed(size, 1, partialWindows);
    }

    default LongSequence windowed(int size, int step, boolean partialWindows,
                                  @NotNull ToLongFunction<LongListX> reducer) {
        return windowed(size, step, partialWindows).mapToLong(reducer);
    }

    default LongSequence windowed(int size, int step, @NotNull ToLongFunction<LongListX> reducer) {
        return windowed(size, step, false, reducer);
    }

    default LongSequence windowed(int size, @NotNull ToLongFunction<LongListX> reducer) {
        return windowed(size, 1, reducer);
    }

    default LongSequence windowed(int size, boolean partialWindows, @NotNull ToLongFunction<LongListX> reducer) {
        return windowed(size, 1, partialWindows, reducer);
    }
}
