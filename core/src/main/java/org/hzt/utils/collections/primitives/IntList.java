package org.hzt.utils.collections.primitives;

import org.hzt.utils.arrays.ArraysX;
import org.hzt.utils.collections.BinarySearchable;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterables.primitives.PrimitiveSortable;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.primitives.IntSequence;

import java.util.Arrays;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public interface IntList extends IntCollection,
        PrimitiveList<PrimitiveListIterator.OfInt>,
        PrimitiveSortable<IntComparator>,
        BinarySearchable<IntUnaryOperator> {

    static IntList empty() {
        return new IntImmutableList();
    }

    static IntList of(final Iterable<Integer> iterable) {
        return IntSequence.of(iterable).toList();
    }

    static IntList of(final int... array) {
        return new IntImmutableList(array);
    }

    static IntList copyOf(final IntCollection intCollection) {
        return new IntImmutableList(intCollection);
    }

    static IntList build(final Consumer<? super IntMutableList> factory) {
        final var listX = IntMutableList.empty();
        factory.accept(listX);
        return listX;
    }

    default boolean contains(final int value) {
        return indexOf(value) >= 0;
    }

    @Override
    default IntList reversed() {
        return IntSequence.reverseOf(this).toList();
    }

    int get(int index);

    int indexOf(int i);

    int lastIndexOf(int i);

    default int lastIndex() {
        return size() - 1;
    }

    @Override
    default int first() {
        return get(0);
    }

    @Override
    default int last() {
        return get(lastIndex());
    }

    @Override
    default OptionalInt findLast() {
        final var lastIndex = lastIndex();
        return lastIndex < 0 ? OptionalInt.empty() : OptionalInt.of(get(lastIndex));
    }

    @Override
    default OptionalInt findLast(final IntPredicate predicate) {
        final var iterator = listIterator(lastIndex());
        while (iterator.hasPrevious()) {
            final var previousInt = iterator.previousInt();
            if (predicate.test(previousInt)) {
                return OptionalInt.of(previousInt);
            }
        }
        return OptionalInt.empty();
    }

    default int random() {
        return findRandom().orElseThrow();
    }

    OptionalInt findRandom();

    @Override
    default ListX<Integer> boxed() {
        return asSequence().boxed().toListX();
    }

    @Override
    default IntList sorted() {
        final var array = toArray();
        Arrays.sort(array);
        return IntList.of(array);
    }

    @Override
    default IntList sorted(final IntComparator comparator) {
        final var array = toArray();
        ArraysX.sort(comparator, array);
        return IntList.of(array);
    }

    @Override
    default IntList sortedDescending() {
        final var array = toArray();
        Arrays.sort(array);
        ArraysX.reverse(array);
        return IntList.of(array);
    }

    IntList shuffled();

    /**
     * @see BinarySearchable#binarySearch(int, int, Object)
     * @see java.util.Arrays#binarySearch(int[], int)
     */
    default int binarySearch(final int fromIndex, final int toIndex, final IntUnaryOperator comparison) {
        return BinarySearchable.binarySearch(size(), fromIndex, toIndex, mid -> comparison.applyAsInt(get(mid)));
    }

    default int binarySearch(final int valueToSearch) {
        return binarySearch(0, size(), e -> Integer.compare(e, valueToSearch));
    }

    @Override
    default IntRange indices() {
        return IntRange.of(0, size());
    }
}
