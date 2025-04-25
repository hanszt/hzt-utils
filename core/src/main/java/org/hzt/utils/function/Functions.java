package org.hzt.utils.function;

import static org.hzt.utils.function.Predicate.not;

public final class Functions {

    private Functions() {
    }

    public static <T> Predicate<T> isNull() {
        return new Predicate<T>() {

            public boolean test(final T t) {
                return t == null;
            }
        };
    }

    public static <T> Predicate<T> isNotNull() {
        return not(isNull());
    }

    public static <T extends Comparable<T>> Predicate<T> lessThan(final T other) {
        return new Predicate<T>() {
            public boolean test(final T item) {
                return item.compareTo(other) < 0;
            }
        };
    }

    public static <T extends Comparable<T>> Predicate<T> lessThanEqual(final T other) {
        return new Predicate<T>() {
            public boolean test(final T item) {
                return item.compareTo(other) <= 0;
            }
        };
    }

    public static <T extends Comparable<T>> Predicate<T> greaterThan(final T other) {
        return new Predicate<T>() {
            public boolean test(final T item) {
                return item.compareTo(other) > 0;
            }
        };
    }

    public static <T extends Comparable<T>> Predicate<T> greaterThanEqual(final T other) {
        return new Predicate<T>() {
            public boolean test(final T item) {
                return item.compareTo(other) >= 0;
            }
        };
    }

    public static <T extends Comparable<T>> BiFunction<T, T, T> max() {
        return new BiFunction<T, T, T>() {

            public T apply(final T t1, final T t2) {
                return t1.compareTo(t2) > 0 ? t1 : t2;
            }
        };
    }
}
