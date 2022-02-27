package hzt.sequences.primitives;

import hzt.collections.primitives.IntListX;
import hzt.iterables.primitives.IntIterable;
import hzt.iterators.primitives.IntWindowedIterator;
import hzt.sequences.Sequence;
import hzt.sequences.SequenceHelper;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;

@FunctionalInterface
public interface IntWindowedSequence extends IntIterable {

    default Sequence<IntListX> chunked(int size) {
        return windowed(size, size, true);
    }

    default Sequence<IntListX> chunked(IntSupplier nextSizeSupplier) {
        return chunked(nextSizeSupplier.getAsInt(), size -> nextSizeSupplier.getAsInt());
    }

    default Sequence<IntListX> chunked(int initSize, IntUnaryOperator nextSizeSupplier) {
        //The holding consumer provides a way to provide the same value to both the size and step unary operator.
        //It also makes sure the nextSizeSupplier from this method is only called once instead of twice
        //It is required that the call for next size is made before next step for them to receive the same value
        final PrimitiveSequenceHelper.HoldingConsumer holdingConsumer = new PrimitiveSequenceHelper.HoldingConsumer();
        return windowed(initSize, size -> {
            final int nextSize = nextSizeSupplier.applyAsInt(size);
            holdingConsumer.accept(nextSize);
            return nextSize;
        }, initSize, step -> holdingConsumer.getValue(), true);
    }

    default Sequence<IntListX> windowed(int size, int step, boolean partialWindows) {
        return windowed(size, It::asInt, step, It::asInt, partialWindows);
    }

    default Sequence<IntListX> windowed(int initSize,
                                        @NotNull IntUnaryOperator nextSizeSupplier,
                                        int initStep,
                                        @NotNull IntUnaryOperator nextStepSupplier,
                                        boolean partialWindows) {
        SequenceHelper.checkInitWindowSizeAndStep(initSize, initStep);
        return () -> IntWindowedIterator.of(iterator(), initSize, nextSizeSupplier, initStep, nextStepSupplier, partialWindows);
    }

    default Sequence<IntListX> windowed(int size, int step) {
        return windowed(size, step, false);
    }

    default Sequence<IntListX> windowed(int size) {
        return windowed(size, 1);
    }

    default Sequence<IntListX> windowed(int size, boolean partialWindows) {
        return windowed(size, 1, partialWindows);
    }

    default IntSequence windowed(int size, int step, boolean partialWindows,
                                 @NotNull ToIntFunction<IntListX> reducer) {
        return windowed(size, step, partialWindows).mapToInt(reducer);
    }

    default IntSequence windowed(int size, int step, @NotNull ToIntFunction<IntListX> reducer) {
        return windowed(size, step, false, reducer);
    }

    default IntSequence windowed(int size, @NotNull ToIntFunction<IntListX> reducer) {
        return windowed(size, 1, reducer);
    }

    default IntSequence windowed(int size, boolean partialWindows, @NotNull ToIntFunction<IntListX> reducer) {
        return windowed(size, 1, partialWindows, reducer);
    }
}
