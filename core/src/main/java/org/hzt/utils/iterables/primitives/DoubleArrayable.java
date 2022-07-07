package org.hzt.utils.iterables.primitives;

import org.hzt.utils.arrays.primitves.PrimitiveArrays;
import org.hzt.utils.function.primitives.DoubleToByteFunction;
import org.hzt.utils.function.primitives.DoubleToFloatFunction;
import org.hzt.utils.function.primitives.DoubleToShortFunction;
import org.hzt.utils.sequences.primitives.DoubleSequence;

import java.util.function.DoublePredicate;

@FunctionalInterface
public interface DoubleArrayable extends
        PrimitiveArrayable<DoublePredicate, DoubleToByteFunction, DoubleToShortFunction, DoubleToFloatFunction>,
        PrimitiveIterable.OfDouble {

    @Override
    default boolean[] toBooleanArray(DoublePredicate mapper) {
        return PrimitiveArrays.toBooleanArray(mapper, DoubleSequence.of(this).toArray());
    }

    @Override
    default byte[] toByteArray(DoubleToByteFunction mapper) {
        return PrimitiveArrays.toByteArray(mapper, DoubleSequence.of(this).toArray());
    }

    @Override
    default short[] toShortArray(DoubleToShortFunction mapper) {
        return PrimitiveArrays.toShortArray(mapper, DoubleSequence.of(this).toArray());
    }

    @Override
    default float[] toFloatArray(DoubleToFloatFunction mapper) {
        return PrimitiveArrays.toFloatArray(mapper, DoubleSequence.of(this).toArray());
    }
}
