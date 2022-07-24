package org.hzt.utils.iterables.primitives;

import org.hzt.utils.primitive_comparators.PrimitiveComparator;

/**
 * @param <C> the primitive comparator
 */
public interface PrimitiveSortable<C extends PrimitiveComparator> {

    PrimitiveSortable<C> sorted();

    PrimitiveSortable<C> sorted(C comparator);

    PrimitiveSortable<C> sortedDescending();
}
