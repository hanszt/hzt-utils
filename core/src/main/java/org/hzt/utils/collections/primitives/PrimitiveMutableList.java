package org.hzt.utils.collections.primitives;

/**
 * @param <L> the primitive list iterator
 * @param <C> the primitive comparator
 */
public interface PrimitiveMutableList<L, C> extends PrimitiveList<L> {

    void sort(C comparator);

    void sort();
}
