package org.hzt.utils.function.predicates;

import org.hzt.utils.sequences.Sequence;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public final class CollectionPredicates {

    private CollectionPredicates() {
    }

    public static <E> Predicate<Collection<E>> containsAll(final Iterable<E> other) {
        return collection -> collection != null && other != null && Sequence.of(other).all(collection::contains);
    }

    public static <E> Predicate<Collection<E>> containsAll(final Collection<E> other) {
        return collection -> collection != null && other != null && collection.containsAll(other);
    }

    @SafeVarargs
    public static <E> Predicate<Collection<E>> containsAll(final E... values) {
        return containsAll(List.of(values));
    }

    public static <E> Predicate<Collection<E>> containsAny(final Iterable<E> other) {
        return collection -> collection != null && other != null && Sequence.of(other).any(collection::contains);
    }

    @SafeVarargs
    public static <E> Predicate<Collection<E>> containsAny(final E... values) {
        return containsAny(List.of(values));
    }

    public static <E> Predicate<Collection<E>> containsNone(final Iterable<E> other) {
        return collection -> collection != null && other != null && Sequence.of(other).none(collection::contains);
    }

    @SafeVarargs
    public static <E> Predicate<Collection<E>> containsNone(final E... values) {
        return containsNone(List.of(values));
    }
}
