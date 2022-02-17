package hzt.iterables;

import hzt.collections.MutableListX;
import hzt.collections.NavigableSetX;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Function;

@FunctionalInterface
public interface Sortable<T> extends Iterable<T> {

    default Sortable<T> shuffled() {
        return IterableXHelper.toSortedListX(this, s -> IterableXHelper.nextRandomDouble());
    }

    default <R extends Comparable<R>> Sortable<T> sorted() {
        return IterableXHelper.toSortedListX(this, (Function<T, R>) IterableXHelper::asComparableOrThrow);
    }

    default <R extends Comparable<R>> Sortable<T> sortedBy(@NotNull Function<? super T, ? extends R> selector) {
        return IterableXHelper.toSortedListX(this, selector);
    }

    default Sortable<T> sortedBy(Comparator<T> comparator) {
        final MutableList<T> list = MutableList.of(this);
        list.sort(comparator);
        return list;
    }

    default <R extends Comparable<R>> Sortable<T> sortedDescending() {
        return sortedByDescending((Function<T, R>) IterableXHelper::asComparableOrThrow);
    }

    default <R extends Comparable<R>> Sortable<T> sortedByDescending(@NotNull Function<? super T, ? extends R> selector) {
        final MutableList<T> list = MutableList.of(this);
        list.sort(Comparator.comparing(selector).reversed());
        return list;
    }

    default <R extends Comparable<R>> SortedMutableSet<T> toSortedSet(@NotNull Function<? super T, ? extends R> selector) {
        SortedMutableSet<T> sortedMutableSet = SortedMutableSet.comparingBy(selector);
        for (T t : this) {
            if (t != null && selector.apply(t) != null) {
                sortedMutableSet.add(t);
            }
        }
        return sortedMutableSet;
    }

    default <R extends Comparable<R>> SortedMutableSet<R> toSortedSetOf(@NotNull Function<? super T, ? extends R> selector) {
        MutableList<R> list = MutableList.empty();
        for (T t : this) {
            if (t != null) {
                final R r = selector.apply(t);
                if (r != null) {
                    list.add(r);
                }
            }
        }
        return SortedMutableSet.of(list, It::self);
    }
}
