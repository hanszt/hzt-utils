package org.hzt.utils.iterables.primitives;


import org.hzt.utils.collections.MapX;
import org.hzt.utils.tuples.Pair;

/**
 * @param <T> the primitive Type
 * @param <L> the primitive list to group to
 * @param <P> the primitive predicate
 */
public interface PrimitiveGroupable<T, L, P> {

    MapX<T, L> group();

    Pair<L, L> partition(P predicate);
}
