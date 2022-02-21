package hzt.iterables.primitives;

import org.jetbrains.annotations.NotNull;

/**
 * @param <T> The numberType
 * @param <B> The primitive binaryOperator
 * @param <P> The primitive predicate
 * @param <O> The primitive optional
 */
public interface PrimitiveReducable<T, B, P, O> extends Iterable<T> {

    @NotNull O reduce(@NotNull B operator);

    boolean any(@NotNull P predicate);

    boolean all(@NotNull P predicate);

    boolean none(@NotNull P predicate);

    O findFirst();

    O findFirst(P predicate);

    O findLast();

    O findLast(P predicate);

    default boolean any() {
        return iterator().hasNext();
    }

    default boolean none() {
        return !iterator().hasNext();
    }
}
