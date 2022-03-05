package org.hzt.utils.collections.primitives;

import org.hzt.utils.arrays.primitves.PrimitiveSort;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterables.primitives.PrimitiveSortable;
import org.hzt.utils.numbers.LongX;
import org.hzt.utils.primitive_comparators.LongComparator;

import java.util.Arrays;
import java.util.function.Consumer;

public interface LongListX extends LongCollection, PrimitiveSortable<LongComparator> {

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
        PrimitiveSort.sort(longs, comparator);
        return LongListX.of(longs);
    }

    @Override
    default LongListX sortedDescending() {
        return sorted(LongX::compareReversed);
    }
}
