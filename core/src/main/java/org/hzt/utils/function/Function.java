package org.hzt.utils.function;

import org.hzt.utils.Objects;

public abstract class Function<T, R> {

    public abstract R apply(T t);

    public static <T> Function<T, T> identity() {
        return new Function<T, T>() {
            public T apply(final T t) {
                return t;
            }
        };
    }

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V>   the type of output of the {@code after} function, and of the
     *              composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    public <V> Function<T, V> andThen(final Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return new Function<T, V>() {

            public V apply(final T t) {
                return after.apply(Function.this.apply(t));
            }
        };
    }
}
