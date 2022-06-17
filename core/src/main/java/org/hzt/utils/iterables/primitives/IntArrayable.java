package org.hzt.utils.iterables.primitives;

import org.hzt.utils.arrays.primitves.PrimitiveArrays;
import org.hzt.utils.function.primitives.IntToByteFunction;
import org.hzt.utils.function.primitives.IntToFloatFunction;
import org.hzt.utils.function.primitives.IntToShortFunction;
import org.hzt.utils.sequences.primitives.IntSequence;

import java.util.function.IntPredicate;

@FunctionalInterface
public interface IntArrayable extends
        PrimitiveArrayable<IntPredicate, IntToByteFunction, IntToShortFunction, IntToFloatFunction>, PrimitiveIterable.OfInt {

    @Override
    default boolean[] toBooleanArray(IntPredicate mapper) {
        return PrimitiveArrays.toBooleanArray(IntSequence.of(this).toArray(), mapper);
    }

    @Override
    default byte[] toByteArray(IntToByteFunction mapper) {
        return PrimitiveArrays.toByteArray(IntSequence.of(this).toArray(), mapper);
    }

    @Override
    default short[] toShortArray(IntToShortFunction mapper) {
        return PrimitiveArrays.toShortArray(IntSequence.of(this).toArray(), mapper);
    }

    @Override
    default float[] toFloatArray(IntToFloatFunction mapper) {
        return PrimitiveArrays.toFloatArray(IntSequence.of(this).toArray(), mapper);
    }
}
