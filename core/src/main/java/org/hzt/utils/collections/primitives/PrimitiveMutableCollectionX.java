package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableCollectionX;
import org.jetbrains.annotations.NotNull;

/**
 * @param <T> The boxed type
 * @param <C> The primitive consumer
 * @param <P> The primitive predicate
 * @param <A> The primitive array type
 */
public interface PrimitiveMutableCollectionX<T, C, P, A> extends PrimitiveCollectionX<T, C, A> {

    boolean addAll(@NotNull Iterable<T> iterable);

    boolean addAll(@NotNull A array);

    boolean removeAll(@NotNull Iterable<T> iterable);

    boolean removeAll(@NotNull A array);

    boolean removeIf(@NotNull P predicate);

    PrimitiveMutableCollectionX<T, C, P, A> plus(@NotNull Iterable<T> iterable);

    PrimitiveMutableCollectionX<T, C, P, A> plus(@NotNull A array);

    MutableCollectionX<T> boxed();

    void clear();
}
