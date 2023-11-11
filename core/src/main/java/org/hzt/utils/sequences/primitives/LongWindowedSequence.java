package org.hzt.utils.sequences.primitives;

import org.hzt.utils.It;
import org.hzt.utils.collections.primitives.LongList;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.LongWindowedIterator;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.SequenceHelper;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.ToLongFunction;

public interface LongWindowedSequence extends PrimitiveIterable.OfLong {

    default Sequence<LongList> chunked(final int size) {
        return windowed(size, size, true);
    }

    default Sequence<LongList> chunked(final IntSupplier nextSizeSupplier) {
        return chunked(nextSizeSupplier.getAsInt(), size -> nextSizeSupplier.getAsInt());
    }

    default Sequence<LongList> chunked(final int initSize, final IntUnaryOperator nextSizeSupplier) {
        //The holding consumer provides a way to provide the same value to both the size and step unary operator.
        //It also makes sure the nextSizeSupplier from this method is only called once instead of twice
        //It is required that the call for next size is made before next step for them to receive the same value
        final var holdingConsumer = new AtomicInteger();
        return windowed(initSize, size -> {
            final var nextSize = nextSizeSupplier.applyAsInt(size);
            holdingConsumer.set(nextSize);
            return nextSize;
        }, initSize, step -> holdingConsumer.get(), true);
    }

    default Sequence<LongList> windowed(final int size, final int step, final boolean partialWindows) {
        return windowed(size, It::asInt, step, It::asInt, partialWindows);
    }

    default Sequence<LongList> windowed(final int initSize,
                                        final IntUnaryOperator nextSizeSupplier,
                                        final int initStep,
                                        final IntUnaryOperator nextStepSupplier,
                                        final boolean partialWindows) {
        SequenceHelper.checkInitWindowSizeAndStep(initSize, initStep);
        return () -> LongWindowedIterator.of(iterator(), initSize, nextSizeSupplier, initStep, nextStepSupplier, partialWindows);
    }

    default Sequence<LongList> windowed(final int size, final int step) {
        return windowed(size, step, false);
    }

    default Sequence<LongList> windowed(final int size) {
        return windowed(size, 1);
    }

    default Sequence<LongList> windowed(final int size, final boolean partialWindows) {
        return windowed(size, 1, partialWindows);
    }

    default LongSequence windowed(final int size, final int step, final boolean partialWindows,
                                  final ToLongFunction<LongList> reducer) {
        return windowed(size, step, partialWindows).mapToLong(reducer);
    }

    default LongSequence windowed(final int size, final int step, final ToLongFunction<LongList> reducer) {
        return windowed(size, step, false, reducer);
    }

    default LongSequence windowed(final int size, final ToLongFunction<LongList> reducer) {
        return windowed(size, 1, reducer);
    }

    default LongSequence windowed(final int size, final boolean partialWindows, final ToLongFunction<LongList> reducer) {
        return windowed(size, 1, partialWindows, reducer);
    }
}
