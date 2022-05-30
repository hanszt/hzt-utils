package org.hzt.utils.collections.primitives;

/**
 * @param <L> the primitive list iterator
 * @param <C> the primitive comparator
 */
public interface PrimitiveMutableList<L, C> {

    L listIterator();

    L listIterator(int index);

    void sort(C comparator);

    void sort();
}
