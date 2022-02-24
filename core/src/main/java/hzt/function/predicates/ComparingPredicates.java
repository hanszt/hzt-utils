package hzt.function.predicates;

import java.util.Collection;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

public final class ComparingPredicates {

    private ComparingPredicates() {
    }

    public static <T extends Comparable<? super T>> Predicate<T> greaterThan(final T other) {
        return value -> value.compareTo(other) > 0;
    }

    public static <T extends Comparable<? super T>> Predicate<T> greaterThanOrEq(final T other) {
        return value -> value.compareTo(other) >= 0;
    }

    public static <T extends Comparable<? super T>> Predicate<T> smallerThan(final T other) {
        return value -> value.compareTo(other) < 0;
    }

    public static <T extends Comparable<? super T>> Predicate<T> smallerThanOrEq(final T other) {
        return value -> value.compareTo(other) <= 0;
    }

    public static <T extends Comparable<? super T>> Predicate<T> before(final T other) {
        return smallerThan(other);
    }

    public static <T extends Comparable<? super T>> Predicate<T> after(final T other) {
        return greaterThan(other);
    }

    public static IntPredicate greaterThanInt(final int other) {
        return value -> value > other;
    }

    public static IntPredicate smallerThanInt(final int other) {
        return value -> value < other;
    }

    public static IntPredicate greaterThanOrEqInt(final int other) {
        return value -> value >= other;
    }

    public static IntPredicate smallerThanOrEqInt(final int other) {
        return value -> value <= other;
    }

    public static <T> Predicate<Collection<T>> collectionGreaterThan(int size) {
        return collection -> collection.size() > size;
    }

    public static <T> Predicate<Collection<T>> collectionSmallerThan(int size) {
        return collection -> collection.size() < size;
    }

    public static <T> Predicate<T[]> arrayGreaterThan(int size) {
        return array -> array.length > size;
    }

    public static <T> Predicate<T[]> arraySmallerThan(int size) {
        return array -> array.length < size;
    }

    public static Predicate<int[]> intArrayGreaterThan(int size) {
        return array -> array.length > size;
    }

    public static Predicate<int[]> intArraySmallerThan(int size) {
        return array -> array.length < size;
    }
}
