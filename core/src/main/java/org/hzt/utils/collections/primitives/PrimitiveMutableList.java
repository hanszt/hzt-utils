package org.hzt.utils.collections.primitives;

import org.hzt.utils.primitive_comparators.PrimitiveComparator;

/**
 * @param <L> the primitive list iterator
 * @param <C> the primitive comparator
 */
public interface PrimitiveMutableList<L, C extends PrimitiveComparator> extends PrimitiveList<L> {

    void sort(C comparator);

    void sort();
}
