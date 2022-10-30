package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableCollectionX;
import org.jetbrains.annotations.NotNull;

/**
 * @param <T> The boxed type
 * @param <C> The primitive consumer
 * @param <A> The primitive array type
 * @param <P> The primitive predicate
 */
public interface PrimitiveMutableCollection<T, C, A, P> extends PrimitiveCollection<T, C, A> {

    boolean addAll(@NotNull Iterable<T> iterable);

    boolean addAll(@NotNull A array);

    boolean removeAll(@NotNull Iterable<T> iterable);

    boolean removeAll(@NotNull A array);

    boolean removeIf(@NotNull P predicate);

    PrimitiveMutableCollection<T, C, A, P> plus(@NotNull Iterable<T> iterable);

    PrimitiveMutableCollection<T, C, A, P> plus(@NotNull A array);

    MutableCollectionX<T> boxed();

    void clear();
}
