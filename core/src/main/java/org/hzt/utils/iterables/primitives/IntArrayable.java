package org.hzt.utils.iterables.primitives;

import org.hzt.utils.arrays.primitives.PrimitiveArrays;
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
        return PrimitiveArrays.toBooleanArray(mapper, IntSequence.of(this).toArray());
    }

    @Override
    default byte[] toByteArray(IntToByteFunction mapper) {
        return PrimitiveArrays.toByteArray(mapper, IntSequence.of(this).toArray());
    }

    @Override
    default short[] toShortArray(IntToShortFunction mapper) {
        return PrimitiveArrays.toShortArray(mapper, IntSequence.of(this).toArray());
    }

    @Override
    default float[] toFloatArray(IntToFloatFunction mapper) {
        return PrimitiveArrays.toFloatArray(mapper, IntSequence.of(this).toArray());
    }
}
