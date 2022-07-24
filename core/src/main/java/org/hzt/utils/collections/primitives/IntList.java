package org.hzt.utils.collections.primitives;

import org.hzt.utils.arrays.ArraysX;
import org.hzt.utils.collections.ListHelper;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterables.primitives.PrimitiveSortable;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.markerinterfaces.BinarySearchable;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.hzt.utils.ranges.IntRange;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.OptionalInt;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public interface IntList extends IntCollection,
        PrimitiveList<PrimitiveListIterator.OfInt>,
        PrimitiveSortable<IntComparator>,
        BinarySearchable<IntUnaryOperator> {

    static IntList empty() {
        return new IntArrayList();
    }

    static IntList of(Iterable<Integer> iterable) {
        return new IntArrayList(iterable);
    }

    static IntList of(IntList intList) {
        return new IntArrayList(intList);
    }

    static IntList of(int... array) {
        return new IntArrayList(array);
    }

    static IntList build(Consumer<? super IntMutableList> factory) {
        final IntMutableList listX = IntMutableList.empty();
        factory.accept(listX);
        return listX;
    }

    default boolean contains(int i) {
        return indexOf(i) >= 0;
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
    @NotNull
    default OptionalInt findLast() {
        final int lastIndex = lastIndex();
        return lastIndex < 0 ? OptionalInt.empty() : OptionalInt.of(get(lastIndex));
    }

    @Override
    @NotNull
    default OptionalInt findLast(@NotNull IntPredicate predicate) {
        PrimitiveListIterator.OfInt iterator = listIterator(lastIndex());
        while (iterator.hasPrevious()) {
            final int previousInt = iterator.previousInt();
            if (predicate.test(previousInt)) {
                return OptionalInt.of(previousInt);
            }
        }
        return OptionalInt.empty();
    }

    default int random() {
        return findRandom().orElseThrow(NoSuchElementException::new);
    }

    OptionalInt findRandom();

    @Override
    int[] toArray();

    @Override
    default ListX<Integer> boxed() {
        return asSequence().boxed().toListX();
    }

    @Override
    default IntList sorted() {
        final int[] array = toArray();
        Arrays.sort(array);
        return IntList.of(array);
    }

    @Override
    default IntList sorted(IntComparator comparator) {
        final int[] array = toArray();
        ArraysX.sort(comparator, array);
        return IntList.of(array);
    }

    @Override
    default IntList sortedDescending() {
        return sorted(IntX::compareReversed);
    }

    IntList shuffled();

    /**
     * @see org.hzt.utils.markerinterfaces.BinarySearchable#binarySearch(int, int, Object)
     * @see java.util.Arrays#binarySearch(int[], int)
     */
    default int binarySearch(int fromIndex, int toIndex, IntUnaryOperator comparison) {
        return ListHelper.binarySearch(size(), fromIndex, toIndex, mid -> comparison.applyAsInt(get(mid)));
    }

    @Override
    default IntRange indices() {
        return IntRange.of(0, size());
    }
}
