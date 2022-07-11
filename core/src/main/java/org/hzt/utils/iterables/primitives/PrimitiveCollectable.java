package org.hzt.utils.iterables.primitives;

/**
 * @param <C> the primitive collection type
 */
interface PrimitiveCollectable<C> {

    C toList();

    C toMutableList();

    C toMutableSet();
}
