package org.hzt.utils.iterables;

import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.SortedMutableSetX;
import org.hzt.utils.It;
import org.hzt.utils.comparables.ComparableX;
import org.hzt.utils.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Function;

@FunctionalInterface
public interface Sortable<T> extends Iterable<T> {

    default <R extends Comparable<? super R>> Sortable<T> sorted() {
        return toMutableListSortedBy((Function<T, R>) IterableXHelper::asComparableOrThrow);
    }

    default Sortable<T> sorted(Comparator<? super T> comparator) {
        final MutableListX<T> list = MutableListX.of(this);
        list.sort(comparator);
        return list;
    }

    default <R extends Comparable<? super R>> Sortable<T> sortedBy(@NotNull Function<? super T, ? extends R> selector) {
        return toMutableListSortedBy(selector);
    }

    default Sortable<T> sortedDescending() {
        return sortedByDescending(IterableXHelper::asComparableOrThrow);
    }

    default <R extends Comparable<? super R>> Sortable<T> sortedByDescending(@NotNull Function<? super T, ? extends R> selector) {
        final MutableListX<T> list = MutableListX.of(this);
        list.sort(Comparator.comparing(selector).reversed());
        return list;
    }

    private <R extends Comparable<? super R>> MutableListX<T> toMutableListSortedBy(@NotNull Function<? super T, ? extends R> selector) {
        final MutableListX<T> list = MutableListX.of(this);
        list.sort(Comparator.comparing(selector));
        return list;
    }

    default <R extends Comparable<? super R>> SortedMutableSetX<T> toSortedSet(@NotNull Function<? super T, ? extends R> selector) {
        SortedMutableSetX<T> sortedMutableSet = SortedMutableSetX.comparingBy(selector);
        for (T t : this) {
            if (t != null && selector.apply(t) != null) {
                sortedMutableSet.add(t);
            }
        }
        return sortedMutableSet;
    }

    default <R extends Comparable<? super R>> SortedMutableSetX<R> toSortedSetOf(@NotNull Function<? super T, ? extends R> selector) {
        MutableListX<R> list = MutableListX.empty();
        for (T t : this) {
            if (t != null) {
                final R r = selector.apply(t);
                if (r != null) {
                    list.add(r);
                }
            }
        }
        return SortedMutableSetX.of(list, It::self);
    }

    default <R extends Comparable<? super R>> boolean isSortedBy(Function<? super T, ? extends R> selector) {
        return Sequence.of(this)
                .map(selector)
                .zipWithNext()
                .all(ComparableX::smallerOrEqual);
    }

    /**
     * This method offers O(1 - log(n)) time complexity when called against an unsorted sortable
     * <p>
     * If it is sorted the method has time complexity O(n) (Worst case)
     * <p>
     * Can for example be used before binarySearch
     *
     * @param comparator the comparator to use for testing weather the Sortable is sorted
     * @return true if it is sorted else false
     */
    default boolean isSorted(Comparator<T> comparator) {
        return Sequence.of(this)
                .zipWithNext()
                .all((first, second) -> comparator.compare(first, second) <= 0);
    }
}
