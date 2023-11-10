package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableCollectionX;

/**
 * @param <T> The boxed type
 * @param <C> The primitive consumer
 * @param <A> The primitive array type
 * @param <P> The primitive predicate
 */
public interface PrimitiveMutableCollection<T, C, A, P> extends PrimitiveCollection<T, C, A> {

    boolean addAll(Iterable<T> iterable);

    boolean addAll(A array);

    boolean removeAll(Iterable<T> iterable);

    boolean removeAll(A array);

    boolean removeIf(P predicate);

    PrimitiveMutableCollection<T, C, A, P> plus(Iterable<T> iterable);

    PrimitiveMutableCollection<T, C, A, P> plus(A array);

    MutableCollectionX<T> boxed();

    void clear();
}
