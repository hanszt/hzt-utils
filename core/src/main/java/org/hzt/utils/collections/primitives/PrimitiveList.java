package org.hzt.utils.collections.primitives;

import org.hzt.utils.iterables.Reversable;

/**
 * @param <L> the primitive list iterator
 */
public interface PrimitiveList<L> extends Reversable<PrimitiveList<L>> {

    L listIterator();

    L listIterator(int index);
}
