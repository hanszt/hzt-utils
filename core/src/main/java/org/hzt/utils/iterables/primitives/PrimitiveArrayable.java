package org.hzt.utils.iterables.primitives;

/**
 * @param <B> the boolean mapper
 * @param <B1> the byte mapper
 * @param <S> the short mapper
 * @param <F> the float mapper
 */
interface PrimitiveArrayable<B, B1, S, F> {

    boolean[] toBooleanArray(B mapper);

    byte[] toByteArray(B1 mapper);

    short[] toShortArray(S mapper);

    float[] toFloatArray(F mapper);
}
