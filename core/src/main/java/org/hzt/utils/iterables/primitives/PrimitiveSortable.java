package org.hzt.utils.iterables.primitives;

/**
 * @param <C> the primitive comparator
 */
public interface PrimitiveSortable<C> {

    PrimitiveSortable<C> sorted();

    PrimitiveSortable<C> sorted(C comparator);

    PrimitiveSortable<C> sortedDescending();
}
