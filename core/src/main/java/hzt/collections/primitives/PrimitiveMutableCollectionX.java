package hzt.collections.primitives;

/**
 * @param <T> The boxed type
 * @param <C> The PrimitiveConsumer
 * @param <A> The primitive array Type
 */
public interface PrimitiveMutableCollectionX<T, C, A> extends PrimitiveCollectionX<T, C, A> {

    boolean add(T t);
}
