package hzt.function.predicates;

import hzt.stream.StreamUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

public final class CollectionPredicates {

    private CollectionPredicates() {
    }

    public static <E> Predicate<Collection<E>> containsAll(Iterable<E> other) {
        return collection -> collection != null && other != null && StreamUtils.streamOf(other)
                .allMatch(collection::contains);
    }

    public static <E> Predicate<Collection<E>> containsAll(Collection<E> other) {
        return collection -> collection != null && other != null && collection.containsAll(other);
    }

    @SafeVarargs
    public static <E> Predicate<Collection<E>> containsAll(E... values) {
        return containsAll(Arrays.asList(values));
    }

    public static <E> Predicate<Collection<E>> containsAny(Iterable<E> other) {
        return collection -> collection != null && other != null && StreamUtils.streamOf(other)
                .anyMatch(collection::contains);
    }

    @SafeVarargs
    public static <E> Predicate<Collection<E>> containsAny(E... values) {
        return containsAny(Arrays.asList(values));
    }

    public static <E> Predicate<Collection<E>> containsNone(Iterable<E> other) {
        return collection -> collection != null && other != null && StreamUtils.streamOf(other)
                .noneMatch(collection::contains);
    }

    @SafeVarargs
    public static <E> Predicate<Collection<E>> containsNone(E... values) {
        return containsNone(Arrays.asList(values));
    }
}
