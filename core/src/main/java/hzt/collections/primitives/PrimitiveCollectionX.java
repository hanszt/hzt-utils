package hzt.collections.primitives;

import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;

/**
 * @param <T> The boxed type
 * @param <C> The PrimitiveConsumer
 * @param <A> The primitive array Type
 */
public interface PrimitiveCollectionX<T, C, A> {

    int size();

    boolean isEmpty();

    boolean isNotEmpty();

    boolean contains(T o);

    boolean containsAll(@NotNull Iterable<T> iterable);

    PrimitiveIterator<T, C> iterator();

    A toArray();
}
