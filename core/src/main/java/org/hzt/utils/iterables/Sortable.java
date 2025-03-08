package org.hzt.utils.iterables;

import org.hzt.utils.It;
import org.hzt.utils.collections.CollectionX;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.SortedMutableSetX;
import org.hzt.utils.comparables.ComparableX;
import org.hzt.utils.sequences.Sequence;

import java.util.Collection;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Function;

@FunctionalInterface
public interface Sortable<T> extends Iterable<T> {

    default Sortable<T> shuffled(final Random random) {
        return sortedBy(s -> random.nextInt());
    }

    default <R extends Comparable<? super R>> Sortable<T> sorted() {
        return toListSortedBy((Function<T, R>) IterableXHelper::asComparableOrThrow);
    }

    default Sortable<T> sorted(final Comparator<? super T> comparator) {
        return ListX.build(sizeIfKnown(), ml -> {
           ml.addAll(this);
           ml.sort(comparator);
        });
    }

    default <R extends Comparable<? super R>> Sortable<T> sortedBy(final Function<? super T, ? extends R> selector) {
        return toListSortedBy(selector);
    }

    default Sortable<T> sortedDescending() {
        return sortedByDescending(IterableXHelper::asComparableOrThrow);
    }

    default <R extends Comparable<? super R>> Sortable<T> sortedByDescending(final Function<? super T, ? extends R> selector) {
        return ListX.build(sizeIfKnown(), ml -> {
            ml.addAll(this);
            ml.sort(Comparator.comparing(selector).reversed());
        });
    }

    private <R extends Comparable<? super R>> ListX<T> toListSortedBy(final Function<? super T, ? extends R> selector) {
        final var size = sizeIfKnown();
        return ListX.build(size, ml -> {
            ml.addAll(this);
            ml.sort(Comparator.comparing(selector));
        });
    }

    private int sizeIfKnown() {
        return switch (this) {
            case Collection<?> c -> c.size();
            case CollectionX<?> c -> c.size();
            default -> 0;
        };
    }

    default <R extends Comparable<? super R>> SortedMutableSetX<T> toSortedSet(final Function<? super T, ? extends R> selector) {
        final SortedMutableSetX<T> sortedMutableSet = SortedMutableSetX.comparingBy(selector);
        for (final var t : this) {
            if (t != null && selector.apply(t) != null) {
                sortedMutableSet.add(t);
            }
        }
        return sortedMutableSet;
    }

    default <R extends Comparable<? super R>> SortedMutableSetX<R> toSortedSetOf(final Function<? super T, ? extends R> selector) {
        final var list = MutableListX.<R>empty();
        for (final var t : this) {
            if (t != null) {
                final var r = selector.apply(t);
                if (r != null) {
                    list.add(r);
                }
            }
        }
        return SortedMutableSetX.of(list, It::self);
    }

    default <R extends Comparable<? super R>> boolean isSortedBy(final Function<? super T, ? extends R> selector) {
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
    default boolean isSorted(final Comparator<T> comparator) {
        return Sequence.of(this)
                .zipWithNext()
                .all((first, second) -> comparator.compare(first, second) <= 0);
    }
}
