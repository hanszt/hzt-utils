package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableCollectionX;
import org.jetbrains.annotations.NotNull;

/**
 * @param <T> The boxed type
 * @param <C> The PrimitiveConsumer
 * @param <A> The primitive array Type
 */
public interface PrimitiveMutableCollectionX<T, C, A> extends PrimitiveCollectionX<T, C, A> {

    boolean addAll(@NotNull Iterable<T> iterable);

    boolean addAll(@NotNull A array);

    boolean removeAll(@NotNull Iterable<T> iterable);

    boolean removeAll(@NotNull A array);

    PrimitiveMutableCollectionX<T, C, A> plus(@NotNull Iterable<T> iterable);

    PrimitiveMutableCollectionX<T, C, A> plus(@NotNull A array);

    MutableCollectionX<T> boxed();
}
