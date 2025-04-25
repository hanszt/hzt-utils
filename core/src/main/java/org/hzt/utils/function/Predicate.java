package org.hzt.utils.function;

import org.hzt.utils.Objects;

public abstract class Predicate<T> {

    public abstract boolean test(T t);

    public static <T> Predicate<T> not(final Predicate<? super T> predicate) {
        return new Predicate<T>() {

            public boolean test(final T t) {
                return !predicate.test(t);
            }
        };
    }

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
    public Predicate<T> and(final Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return new Predicate<T>() {

            public boolean test(final T t) {
                return Predicate.this.test(t) && other.test(t);
            }
        };
    }

    public static <T> Predicate<T> isEqual(final Object targetRef) {
        return new Predicate<T>() {

            public boolean test(final T t) {
                return targetRef == null ? t == null : targetRef.equals(t);
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
    public Predicate<T> negate() {
        return new Predicate<T>() {
            public boolean test(final T t) {
                return !Predicate.this.test(t);
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
    public Predicate<T> or(final Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return new Predicate<T>() {
            public boolean test(final T t) {
                return Predicate.this.test(t) || other.test(t);
            }
        };
    }
}
