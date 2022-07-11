package org.hzt.utils.iterables.primitives;

/**
 * @param <C> the primitive collection type
 */
interface PrimitiveCollectable<C> {

    C toListX();

    C toMutableList();

    C toMutableSetX();
}
