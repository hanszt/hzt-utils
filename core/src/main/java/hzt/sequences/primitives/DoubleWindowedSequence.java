package hzt.sequences.primitives;

import hzt.collections.primitives.DoubleListX;
import hzt.iterables.primitives.DoubleIterable;
import hzt.iterators.primitives.DoubleWindowedIterator;
import hzt.sequences.Sequence;
import hzt.sequences.SequenceHelper;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.ToDoubleFunction;

@FunctionalInterface
public interface DoubleWindowedSequence extends DoubleIterable {

    default Sequence<DoubleListX> chunked(int size) {
        return windowed(size, size, true);
    }

    default Sequence<DoubleListX> chunked(IntSupplier nextSizeSupplier) {
        return chunked(nextSizeSupplier.getAsInt(), size -> nextSizeSupplier.getAsInt());
    }

    default Sequence<DoubleListX> chunked(int initSize, IntUnaryOperator nextSizeSupplier) {
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

    default Sequence<DoubleListX> windowed(int size, int step, boolean partialWindows) {
        return windowed(size, It::asInt, step, It::asInt, partialWindows);
    }

    default Sequence<DoubleListX> windowed(int initSize,
                                           @NotNull IntUnaryOperator nextSizeSupplier,
                                           int initStep,
                                           @NotNull IntUnaryOperator nextStepSupplier,
                                           boolean partialWindows) {
        SequenceHelper.checkInitWindowSizeAndStep(initSize, initStep);
        return () -> DoubleWindowedIterator.of(iterator(), initSize, nextSizeSupplier, initStep, nextStepSupplier, partialWindows);
    }

    default Sequence<DoubleListX> windowed(int initSize,
                                           @NotNull IntUnaryOperator nextSizeSupplier,
                                           int initStep,
                                           @NotNull IntUnaryOperator nextStepSupplier) {
        return windowed(initSize, nextSizeSupplier, initStep, nextStepSupplier, false);
    }

    default Sequence<DoubleListX> windowed(int size, int step) {
        return windowed(size, step, false);
    }

    default Sequence<DoubleListX> windowed(int size) {
        return windowed(size, 1);
    }

    default Sequence<DoubleListX> windowed(int size, boolean partialWindows) {
        return windowed(size, 1, partialWindows);
    }

    default DoubleSequence windowed(int size, int step, boolean partialWindows,
                                    @NotNull ToDoubleFunction<DoubleListX> reducer) {
        return windowed(size, step, partialWindows).mapToDouble(reducer);
    }

    default DoubleSequence windowed(int size, int step, @NotNull ToDoubleFunction<DoubleListX> reducer) {
        return windowed(size, step, false, reducer);
    }

    default DoubleSequence windowed(int size, @NotNull ToDoubleFunction<DoubleListX> reducer) {
        return windowed(size, 1, reducer);
    }

    default DoubleSequence windowed(int size, boolean partialWindows, @NotNull ToDoubleFunction<DoubleListX> reducer) {
        return windowed(size, 1, partialWindows, reducer);
    }
}
