package hzt.function;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface QuintFunction<T, U, V, W, X, R> {

    R apply(T t, U u, V v, W a, X x);

    /**
     * Returns first composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <Y> the type of output of the {@code after} function, and of the
     *           composed function
     * @param after the function to apply after this function is applied
     * @return first composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    default <Y> QuintFunction<T, U, V, W, X, Y> andThen(Function<? super R, ? extends Y> after) {
        Objects.requireNonNull(after);
        return (T t, U u, V v, W w, X x) -> after.apply(apply(t, u, v, w, x));
    }

    static <T, U, V, W, X, R> QuintFunction<T, U, V, W, X, R> of(QuintFunction<T, U, V, W, X, R> function) {
        Objects.requireNonNull(function);
        return function;
    }
}
