package org.hzt.utils.collections.primitives;

import org.hzt.utils.arrays.primitves.PrimitiveArrays;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterables.primitives.PrimitiveSortable;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.markerinterfaces.BinarySearchable;
import org.hzt.utils.numbers.LongX;
import org.hzt.utils.primitive_comparators.LongComparator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.OptionalLong;
import java.util.function.Consumer;
import java.util.function.LongPredicate;
import java.util.function.LongToIntFunction;

public interface LongListX extends LongCollection,
        PrimitiveList<PrimitiveListIterator.OfLong>,
        PrimitiveSortable<LongComparator>,
        BinarySearchable<LongToIntFunction> {

    static LongListX empty() {
        return new LongArrayList();
    }

    static LongListX of(Iterable<Long> iterable) {
        return new LongArrayList(iterable);
    }

    static LongListX of(LongListX longListX) {
        return new LongArrayList(longListX);
    }

    static LongListX of(long... array) {
        return new LongArrayList(array);
    }

    static LongListX build(Consumer<LongMutableListX> factory) {
        final LongMutableListX listX = LongMutableListX.empty();
        factory.accept(listX);
        return listX;
    }

    default boolean contains(long l) {
        return indexOf(l) >= 0;
    }

    long get(int index);

    int indexOf(long l);

    int lastIndexOf(long l);

    default int lastIndex() {
        return size() - 1;
    }

    @Override
    default long first() {
        return get(0);
    }

    @Override
    default long last() {
        return get(lastIndex());
    }

    @Override
    @NotNull
    default OptionalLong findLast() {
        final var lastIndex = lastIndex();
        return lastIndex < 0 ? OptionalLong.empty() : OptionalLong.of(get(lastIndex));
    }

    @Override
    @NotNull
    default OptionalLong findLast(@NotNull LongPredicate predicate) {
        PrimitiveListIterator.OfLong iterator = listIterator(lastIndex());
        while (iterator.hasPrevious()) {
            final var previousLong = iterator.previousLong();
            if (predicate.test(previousLong)) {
                return OptionalLong.of(previousLong);
            }
        }
        return OptionalLong.empty();
    }

    @Override
    ListX<Long> boxed();

    @Override
    default LongListX sorted() {
        final var array = toArray();
        Arrays.sort(array);
        return LongListX.of(array);
    }

    @Override
    default LongListX sorted(LongComparator comparator) {
        final var longs = toArray();
        PrimitiveArrays.sort(longs, comparator);
        return LongListX.of(longs);
    }

    @Override
    default LongListX sortedDescending() {
        return sorted(LongX::compareReversed);
    }

    @Override
    default boolean isSorted(LongComparator comparator) {
        final var iterator = iterator();
        if (iterator.hasNext()) {
            var first = iterator.nextLong();
            while (iterator.hasNext()) {
                final var second = iterator.nextLong();
                if (comparator.compareLong(first, second) > 0) {
                    return false;
                }
                first = second;
            }
        }
        return true;
    }

    @Override
    default boolean isSorted() {
        return isSorted(Long::compare);
    }

    /**
     * @see org.hzt.utils.markerinterfaces.BinarySearchable#binarySearch(int, int, Object)
     * @see java.util.Arrays#binarySearch(long[], long)
     */
    @Override
    default int binarySearch(int fromIndex, int toIndex, LongToIntFunction comparison) {
        return PrimitiveListHelper.binarySearch(size(), this::get, fromIndex, toIndex, comparison);
    }
}
