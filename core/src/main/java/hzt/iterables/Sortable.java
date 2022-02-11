package hzt.iterables;

import hzt.collections.ListX;
import hzt.collections.MutableListX;
import hzt.collections.NavigableSetX;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Function;

public interface Sortable<T> extends Iterable<T> {

    private <R extends Comparable<R>> ListX<T> toSortedListX(@NotNull Function<? super T, ? extends R> selector) {
        return toMutableListSortedBy(selector);
    }

    default Sortable<T> shuffled() {
        return toSortedListX(s -> IterableXHelper.nextRandomDouble());
    }

    default <R extends Comparable<R>> Sortable<T> sorted() {
        return toSortedListX((Function<T, R>) IterableXHelper::asComparableOrThrow);
    }

    default <R extends Comparable<R>> Sortable<T> sortedBy(@NotNull Function<? super T, ? extends R> selector) {
        return toSortedListX(selector);
    }

    default <R extends Comparable<R>> Sortable<T> sortedBy(Comparator<T> comparator) {
        final MutableListX<T> list = MutableListX.of(this);
        list.sort(comparator);
        return list;
    }

    default <R extends Comparable<R>> Sortable<T> sortedDescending() {
        return sortedByDescending((Function<T, R>) IterableXHelper::asComparableOrThrow);
    }

    default <R extends Comparable<R>> Sortable<T> sortedByDescending(@NotNull Function<? super T, ? extends R> selector) {
        final MutableListX<T> list = MutableListX.of(this);
        list.sort(Comparator.comparing(selector).reversed());
        return list;
    }

    private <R extends Comparable<R>> MutableListX<T> toMutableListSortedBy(@NotNull Function<? super T, ? extends R> selector) {
        final MutableListX<T> list = MutableListX.of(this);
        list.sort(Comparator.comparing(selector));
        return list;
    }

    default <R extends Comparable<R>> NavigableSetX<T> toSortedSet(@NotNull Function<? super T, ? extends R> selector) {
        NavigableSetX<T> navigableSetX = NavigableSetX.comparingBy(selector);
        for (T t : this) {
            if (t != null && selector.apply(t) != null) {
                navigableSetX.add(t);
            }
        }
        return navigableSetX;
    }

    default <R extends Comparable<R>> NavigableSetX<R> toSortedSetOf(@NotNull Function<? super T, ? extends R> selector) {
        MutableListX<R> listX = MutableListX.empty();
        for (T t : this) {
            if (t != null) {
                final R r = selector.apply(t);
                if (r != null) {
                    listX.add(r);
                }
            }
        }
        return NavigableSetX.of(listX, It::self);
    }
}
