package org.hzt.utils.collections.primitives;

/**
 * @param <L> the primitive list iterator
 */
public interface PrimitiveList<L> {

    L listIterator();

    L listIterator(int index);
}
