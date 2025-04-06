package org.hzt.utils.function;

import org.hzt.utils.Objects;

public final class Functions {

    private Functions() {
    }

    public static <T> Function<T, T> identity() {
        return new Function<T, T>() {
            public T apply(final T t) {
                return t;
            }
        };
    }

    public static <T> AbstractPredicate<T> isEqual(final Object targetRef) {
        return new AbstractPredicate<T>() {

            public boolean test(final T t) {
                return targetRef == null ? t == null : targetRef.equals(t);
            }
        };
    }

    public static <T extends Comparable<T>> AbstractPredicate<T> lessThan(final T other) {
        return new AbstractPredicate<T>() {
            public boolean test(final T item) {
                return item.compareTo(other) < 0;
            }
        };
    }

    public static <T extends Comparable<T>> AbstractPredicate<T> lessThanEqual(final T other) {
        return new AbstractPredicate<T>() {
            public boolean test(final T item) {
                return item.compareTo(other) <= 0;
            }
        };
    }

    public static <T extends Comparable<T>> AbstractPredicate<T> greaterThan(final T other) {
        return new AbstractPredicate<T>() {
            public boolean test(final T item) {
                return item.compareTo(other) > 0;
            }
        };
    }

    public static <T extends Comparable<T>> AbstractPredicate<T> greaterThanEqual(final T other) {
        return new AbstractPredicate<T>() {
            public boolean test(final T item) {
                return item.compareTo(other) >= 0;
            }
        };
    }

    public static abstract class AbstractFunction<T, R> implements Function<T, R> {

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
        public <V> AbstractFunction<T, V> andThen(final Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return new AbstractFunction<T, V>() {

                public V apply(final T t) {
                    return after.apply(AbstractFunction.this.apply(t));
                }
            };
        }
    }

    public static abstract class AbstractPredicate<T> implements Predicate<T> {

        /**
         * Returns a composed predicate that represents a short-circuiting logical
         * AND of this predicate and another.  When evaluating the composed
         * predicate, if this predicate is {@code false}, then the {@code other}
         * predicate is not evaluated.
         *
         * <p>Any exceptions thrown during evaluation of either predicate are relayed
         * to the caller; if evaluation of this predicate throws an exception, the
         * {@code other} predicate will not be evaluated.
         *
         * @param other a predicate that will be logically-ANDed with this
         *              predicate
         * @return a composed predicate that represents the short-circuiting logical
         * AND of this predicate and the {@code other} predicate
         * @throws NullPointerException if other is null
         */
        public AbstractPredicate<T> and(final Predicate<? super T> other) {
            Objects.requireNonNull(other);
            return new AbstractPredicate<T>() {

                public boolean test(final T t) {
                    return AbstractPredicate.this.test(t) && other.test(t);
                }
            };
        }

        /**
         * Returns a predicate that represents the logical negation of this
         * predicate.
         *
         * @return a predicate that represents the logical negation of this
         * predicate
         */
        public AbstractPredicate<T> negate() {
            return new AbstractPredicate<T>() {
                public boolean test(final T t) {
                    return !AbstractPredicate.this.test(t);
                }
            };
        }

        /**
         * Returns a composed predicate that represents a short-circuiting logical
         * OR of this predicate and another.  When evaluating the composed
         * predicate, if this predicate is {@code true}, then the {@code other}
         * predicate is not evaluated.
         *
         * <p>Any exceptions thrown during evaluation of either predicate are relayed
         * to the caller; if evaluation of this predicate throws an exception, the
         * {@code other} predicate will not be evaluated.
         *
         * @param other a predicate that will be logically-ORed with this
         *              predicate
         * @return a composed predicate that represents the short-circuiting logical
         * OR of this predicate and the {@code other} predicate
         * @throws NullPointerException if other is null
         */
        public AbstractPredicate<T> or(final Predicate<? super T> other) {
            Objects.requireNonNull(other);
            return new AbstractPredicate<T>() {
                public boolean test(final T t) {
                    return AbstractPredicate.this.test(t) || other.test(t);
                }
            };
        }
    }
}
