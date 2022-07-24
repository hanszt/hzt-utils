package org.hzt.utils.collections.primitives;

import org.hzt.utils.arrays.ArraysX;
import org.hzt.utils.collections.ListHelper;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterables.primitives.PrimitiveSortable;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.markerinterfaces.BinarySearchable;
import org.hzt.utils.primitive_comparators.DoubleComparator;
import org.hzt.utils.ranges.IntRange;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.DoublePredicate;
import java.util.function.DoubleToIntFunction;

public interface DoubleList extends DoubleCollection,
        PrimitiveList<PrimitiveListIterator.OfDouble>,
        PrimitiveSortable<DoubleComparator>,
        BinarySearchable<DoubleToIntFunction> {

    static DoubleList empty() {
        return new DoubleArrayList();
    }

    static DoubleList of(Iterable<Double> iterable) {
        return new DoubleArrayList(iterable);
    }

    static DoubleList of(DoubleList doubleList) {
        return new DoubleArrayList(doubleList);
    }

    static DoubleList of(double... array) {
        return new DoubleArrayList(array);
    }

    static DoubleList build(Consumer<? super DoubleMutableList> factory) {
        final DoubleMutableList listX = DoubleMutableList.empty();
        factory.accept(listX);
        return listX;
    }

    default boolean contains(double o) {
        return indexOf(o) >= 0;
    }

    double get(int index);

    int indexOf(double d);

    int lastIndexOf(double d);

    default int lastIndex() {
        return size() - 1;
    }

    @Override
    default double first() {
        return get(0);
    }

    @Override
    default double last() {
        return get(lastIndex());
    }

    @Override
    @NotNull
    default OptionalDouble findLast() {
        final int lastIndex = lastIndex();
        return lastIndex < 0 ? OptionalDouble.empty() : OptionalDouble.of(get(lastIndex));
    }

    @Override
    @NotNull
    default OptionalDouble findLast(@NotNull DoublePredicate predicate) {
        PrimitiveListIterator.OfDouble iterator = listIterator(lastIndex());
        while (iterator.hasPrevious()) {
            final double previousDouble = iterator.previousDouble();
            if (predicate.test(previousDouble)) {
                return OptionalDouble.of(previousDouble);
            }
        }
        return OptionalDouble.empty();
    }

    @Override
    default ListX<Double> boxed() {
        return asSequence().boxed().toListX();
    }

    @Override
    default DoubleList sorted() {
        final double[] array = toArray();
        Arrays.sort(array);
        return DoubleList.of(array);
    }

    @Override
    default DoubleList sorted(DoubleComparator comparator) {
        final double[] array = toArray();
        ArraysX.sort(comparator, array);
        return DoubleList.of(array);
    }

    @Override
    default DoubleList sortedDescending() {
        final double[] array = toArray();
        Arrays.sort(array);
        ArraysX.reverse(array);
        return DoubleList.of(array);
    }

    /**
     * @see org.hzt.utils.markerinterfaces.BinarySearchable#binarySearch(int, int, Object)
     * @see java.util.Arrays#binarySearch(double[], double)
     */
    @Override
    default int binarySearch(int fromIndex, int toIndex, DoubleToIntFunction comparison) {
        return ListHelper.binarySearch(size(), fromIndex, toIndex, mid -> comparison.applyAsInt(get(mid)));
    }

    @Override
    default IntRange indices() {
        return IntRange.of(0, size());
    }
}
