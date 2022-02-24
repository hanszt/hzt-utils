package hzt.collections.primitives;

import hzt.arrays.primitves.LongSort;
import hzt.collections.ListX;
import hzt.iterables.primitives.PrimitiveSortable;
import hzt.numbers.LongX;
import hzt.utils.primitive_comparators.LongComparator;

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

    default boolean contains(long l) {
        return indexOf(l) >= 0;
    }

    int indexOf(long l);

    @Override
    long[] toArray();

    @Override
    default ListX<Long> boxed() {
        return asSequence().boxed().toListX();
    }

    @Override
    default LongListX sorted() {
        return sorted(Long::compare);
    }

    @Override
    default LongListX sorted(LongComparator comparator) {
        final long[] longs = toArray();
        LongSort.sort(longs, comparator);
        return LongListX.of(longs);
    }

    @Override
    default LongListX sortedDescending() {
        return sorted(LongX::compareReversed);
    }
}
