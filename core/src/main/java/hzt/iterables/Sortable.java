package hzt.iterables;

import hzt.collections.MutableListX;
import hzt.collections.SortedMutableSetX;
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

    default <R extends Comparable<? super R>> Sortable<T> sortedBy(@NotNull Function<? super T, ? extends R> selector) {
        return IterableXHelper.toSortedListX(this, selector);
    }

    default Sortable<T> sorted(Comparator<T> comparator) {
        final MutableListX<T> list = MutableListX.of(this);
        list.sort(comparator);
        return list;
    }

    default <R extends Comparable<? super R>> Sortable<T> sortedDescending() {
        return sortedByDescending((Function<T, R>) IterableXHelper::asComparableOrThrow);
    }

    default <R extends Comparable<? super R>> Sortable<T> sortedByDescending(@NotNull Function<? super T, ? extends R> selector) {
        final MutableListX<T> list = MutableListX.of(this);
        list.sort(Comparator.comparing(selector).reversed());
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
}
