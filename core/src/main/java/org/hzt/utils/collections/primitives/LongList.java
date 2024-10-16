package org.hzt.utils.collections.primitives;

import org.hzt.utils.arrays.ArraysX;
import org.hzt.utils.collections.BinarySearchable;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterables.primitives.PrimitiveSortable;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.primitive_comparators.LongComparator;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.primitives.LongSequence;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.OptionalLong;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.LongPredicate;
import java.util.function.LongToIntFunction;

public interface LongList extends LongCollection,
        PrimitiveList<PrimitiveListIterator.OfLong>,
        PrimitiveSortable<LongComparator>,
        BinarySearchable<LongToIntFunction> {

    static LongList empty() {
        return new LongImmutableList();
    }

    static LongList of(final long... array) {
        return new LongImmutableList(array);
    }

    static LongList of(final Iterable<Long> iterable) {
        return LongSequence.of(iterable).toList();
    }

    static LongList copyOf(final LongCollection longCollection) {
        return new LongImmutableList(longCollection);
    }

    static LongList build(final Consumer<? super LongMutableList> factory) {
        final LongMutableList mutableList = LongMutableList.empty();
        factory.accept(mutableList);
        return LongList.copyOf(mutableList);
    }

    default boolean contains(final long value) {
        return indexOf(value) >= 0;
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
    default OptionalLong findLast() {
        final int lastIndex = lastIndex();
        return lastIndex < 0 ? OptionalLong.empty() : OptionalLong.of(get(lastIndex));
    }

    @Override
    default OptionalLong findLast(final LongPredicate predicate) {
        final PrimitiveListIterator.OfLong iterator = listIterator(lastIndex());
        while (iterator.hasPrevious()) {
            final long previousLong = iterator.previousLong();
            if (predicate.test(previousLong)) {
                return OptionalLong.of(previousLong);
            }
        }
        return OptionalLong.empty();
    }

    default long random(final Random random) {
        return findRandom(random).orElseThrow(NoSuchElementException::new);
    }

    OptionalLong findRandom(Random random);

    @Override
    default ListX<Long> boxed() {
        return asSequence().boxed().toListX();
    }

    @Override
    default LongList sorted() {
        final long[] array = toArray();
        Arrays.sort(array);
        return LongList.of(array);
    }

    @Override
    default LongList sorted(final LongComparator comparator) {
        final long[] longs = toArray();
        ArraysX.sort(comparator, longs);
        return LongList.of(longs);
    }

    @Override
    default LongList sortedDescending() {
        final long[] array = toArray();
        Arrays.sort(array);
        ArraysX.reverse(array);
        return LongList.of(array);
    }

    LongList shuffled(Random random);

    /**
     * @see BinarySearchable#binarySearch(int, int, Object)
     * @see java.util.Arrays#binarySearch(long[], long)
     */
    @Override
    default int binarySearch(final int fromIndex, final int toIndex, final LongToIntFunction comparison) {
        return BinarySearchable.binarySearch(size(), fromIndex, toIndex, mid -> comparison.applyAsInt(get(mid)));
    }

    default int binarySearch(final int valueToSearch) {
        return binarySearch(0, size(), e -> Long.compare(e, valueToSearch));
    }

    @Override
    default IntRange indices() {
        return IntRange.of(0, size());
    }
}
