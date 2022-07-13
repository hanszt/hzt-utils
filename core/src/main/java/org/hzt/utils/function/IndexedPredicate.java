package org.hzt.utils.function;

import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
public interface IndexedPredicate<T> extends Predicate<T> {

    boolean test(int index, T value);

    default boolean test(T value) {
        return test(0, value);
    }

    default IndexedPredicate<T> and(IndexedPredicate<? super T> other) {
        Objects.requireNonNull(other);
        return (i, t) -> test(i, t) && other.test(i, t);
    }

    default IndexedPredicate<T> or(IndexedPredicate<? super T> other) {
        Objects.requireNonNull(other);
        return (i, t) -> test(i, t) || other.test(i, t);
    }

    @Override
    default IndexedPredicate<T> negate() {
        return (i, t) -> !test(i, t);
    }

    static <T> IndexedPredicate<T> not(IndexedPredicate<? super T> target) {
        Objects.requireNonNull(target);
        //noinspection unchecked
        return (IndexedPredicate<T>) target.negate();
    }
}
