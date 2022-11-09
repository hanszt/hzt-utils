package org.hzt.utils.function;

import org.hzt.utils.PreConditions;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

public final class Functions {

    private Functions() {
    }

    /**
     * A function that first maps to some other type which can than be used to test with.
     *
     * @param mapper    the mapper that is applied before testing the predicate
     * @param predicate the predicate to be tested
     * @param <T>       the incoming type
     * @param <R>       the type to test the predicate on
     * @return first predicate for the incoming type
     * @throws NullPointerException if the mapper or predicate is null
     * @apiNote This allows for easy filtering by some nested object while maintaining the original object in the stream
     * <p><second>Example:</second>
     * <pre>{@code
     * List<Book> filteredBookList = books.stream()
     *          .filter(by(Book::getAuthor, contains("first")
     *          .or(startsWith("j"))))
     *          .collect(Collectors.toList());
     * }</pre>
     * It can help clean up code
     */
    public static <T, R> Predicate<T> by(Function<? super T, ? extends R> mapper, Predicate<? super R> predicate) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(mapper);
        return t -> {
            final R r = mapper.apply(t);
            return r != null && predicate.test(r);
        };
    }

    public static <T, U, R> Predicate<T> by(
            Function<? super T, ? extends U> toUMapper,
            Function<? super U, ? extends R> toRMapper,
            Predicate<? super R> predicate) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(toUMapper);
        Objects.requireNonNull(toRMapper);
        return t -> {
            final U u = toUMapper.apply(t);
            final R r = u != null ? toRMapper.apply(u) : null;
            return r != null && predicate.test(r);
        };
    }

    public static <T, U, V, R> Predicate<T> by(
            Function<? super T, ? extends U> toUMapper,
            Function<? super U, ? extends V> toVMapper,
            Function<? super V, ? extends R> toRMapper,
            Predicate<? super R> predicate) {
        PreConditions.requireAllNonNull(Object.class, toUMapper, toVMapper, toRMapper, predicate);
        return t -> {
            final U u = toUMapper.apply(t);
            final V v = u != null ? toVMapper.apply(u) : null;
            final R r = u != null ? toRMapper.apply(v) : null;
            return r != null && predicate.test(r);
        };
    }

    /**
     * Allows for easier use of combiner functions 'and()' and 'or()' in the Predicate class
     *
     * @param predicate the input predicate
     * @param <T>       the type
     * @return the input predicate
     * @throws NullPointerException if the predicate is null
     *                              <p><second>Example:</second>
     *                              <pre>{@code
     *                               List<Painting> filteredPaintings = paintings.stream()
     *                                         .filter((by(Painting::isFromPicasso)
     *                                               .or(Painting::isFromRembrandt))
     *                                               .and(Painting::isInMuseum)))
     *                                         .collect(Collectors.toList());
     *                              }</pre>
     * @see java.util.function.Predicate#and(Predicate)
     * @see java.util.function.Predicate#or(Predicate)
     */
    public static <T> Predicate<T> by(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return predicate;
    }

    public static <T, U> BiPredicate<T, U> not(BiPredicate<T, U> predicate) {
        return predicate.negate();
    }

    public static IntPredicate notInt(IntPredicate predicate) {
        return predicate.negate();
    }

    public static LongPredicate notLong(LongPredicate predicate) {
        return predicate.negate();
    }

    public static DoublePredicate notDouble(DoublePredicate predicate) {
        return predicate.negate();
    }

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }

    public static <T, R> Predicate<T> distinctBy(Function<? super T, ? extends R> function) {
        Set<R> seen = new HashSet<>();
        return t -> seen.add(function.apply(t));
    }
}
