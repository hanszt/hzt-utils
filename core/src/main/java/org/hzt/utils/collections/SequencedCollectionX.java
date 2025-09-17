package org.hzt.utils.collections;

import org.hzt.utils.iterables.Reversable;

public interface SequencedCollectionX<E> extends CollectionX<E>, Reversable<SequencedCollectionX<E>> {

    SequencedCollectionX<E> reversed();
}
