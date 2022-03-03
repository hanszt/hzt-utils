package org.hzt.utils.iterables.primitives;

/**
 * @param <C> the primitive collection type
 */
public interface PrimitiveCollectable<C> {

    C toListX();

    C toMutableList();
}
