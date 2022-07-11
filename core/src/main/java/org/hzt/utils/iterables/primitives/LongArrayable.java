package org.hzt.utils.iterables.primitives;

import org.hzt.utils.arrays.primitives.PrimitiveArrays;
import org.hzt.utils.function.primitives.LongToByteFunction;
import org.hzt.utils.function.primitives.LongToFloatFunction;
import org.hzt.utils.function.primitives.LongToShortFunction;
import org.hzt.utils.sequences.primitives.LongSequence;

import java.util.function.LongPredicate;

@FunctionalInterface
public interface LongArrayable extends
        PrimitiveArrayable<LongPredicate, LongToByteFunction, LongToShortFunction, LongToFloatFunction>, PrimitiveIterable.OfLong {

    @Override
    default boolean[] toBooleanArray(LongPredicate mapper) {
        return PrimitiveArrays.toBooleanArray(mapper, LongSequence.of(this).toArray());
    }

    @Override
    default byte[] toByteArray(LongToByteFunction mapper) {
        return PrimitiveArrays.toByteArray(mapper, LongSequence.of(this).toArray());
    }

    @Override
    default short[] toShortArray(LongToShortFunction mapper) {
        return PrimitiveArrays.toShortArray(mapper, LongSequence.of(this).toArray());
    }

    @Override
    default float[] toFloatArray(LongToFloatFunction mapper) {
        return PrimitiveArrays.toFloatArray(mapper, LongSequence.of(this).toArray());
    }
}
