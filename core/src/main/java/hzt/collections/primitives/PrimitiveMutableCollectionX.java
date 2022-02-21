package hzt.collections.primitives;

import hzt.collections.MutableCollectionX;

/**
 * @param <T> The boxed type
 * @param <C> The PrimitiveConsumer
 * @param <A> The primitive array Type
 */
public interface PrimitiveMutableCollectionX<T, C, A> extends PrimitiveCollectionX<T, C, A> {

    boolean add(T t);

    boolean addAll(Iterable<T> iterable);

    PrimitiveMutableCollectionX<T, C, A> plus(Iterable<T> iterable);

    MutableCollectionX<T> boxed();
}
