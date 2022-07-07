package org.hzt.utils.collections.primitives;

import org.hzt.utils.arrays.primitves.PrimitiveArrays;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterables.primitives.PrimitiveSortable;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.markerinterfaces.BinarySearchable;
import org.hzt.utils.numbers.DoubleX;
import org.hzt.utils.primitive_comparators.DoubleComparator;
import org.hzt.utils.ranges.IntRange;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.function.Consumer;
import java.util.function.DoublePredicate;
import java.util.function.DoubleToIntFunction;

public interface DoubleListX extends DoubleCollection,
        PrimitiveList<PrimitiveListIterator.OfDouble>,
        PrimitiveSortable<DoubleComparator>,
        BinarySearchable<DoubleToIntFunction> {

    static DoubleListX empty() {
        return new DoubleArrayList();
    }

    static DoubleListX of(Iterable<Double> iterable) {
        return new DoubleArrayList(iterable);
    }

    static DoubleListX of(DoubleListX doubleListX) {
        return new DoubleArrayList(doubleListX);
    }

    static DoubleListX of(double... array) {
        return new DoubleArrayList(array);
    }

    static DoubleListX build(Consumer<DoubleMutableListX> factory) {
        final DoubleMutableListX listX = DoubleMutableListX.empty();
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
        final var lastIndex = lastIndex();
        return lastIndex < 0 ? OptionalDouble.empty() : OptionalDouble.of(get(lastIndex));
    }

    @Override
    @NotNull
    default OptionalDouble findLast(@NotNull DoublePredicate predicate) {
        PrimitiveListIterator.OfDouble iterator = listIterator(lastIndex());
        while (iterator.hasPrevious()) {
            final var previousDouble = iterator.previousDouble();
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
    default DoubleListX sorted() {
        final var array = toArray();
        Arrays.sort(array);
        return DoubleListX.of(array);
    }

    @Override
    default DoubleListX sorted(DoubleComparator comparator) {
        final var array = toArray();
        PrimitiveArrays.sort(comparator, array);
        return DoubleListX.of(array);
    }

    @Override
    default DoubleListX sortedDescending() {
        return sorted(DoubleX::compareReversed);
    }

    @Override
    default boolean isSorted(DoubleComparator comparator) {
        final var iterator = iterator();
        if (iterator.hasNext()) {
            var first = iterator.nextDouble();
            while (iterator.hasNext()) {
                final var second = iterator.nextDouble();
                if (comparator.compareDouble(first, second) > 0) {
                    return false;
                }
                first = second;
            }
        }
        return true;
    }

    @Override
    default boolean isSorted() {
        return isSorted(Double::compare);
    }

    /**
     * @see org.hzt.utils.markerinterfaces.BinarySearchable#binarySearch(int, int, Object)
     * @see java.util.Arrays#binarySearch(double[], double)
     */
    @Override
    default int binarySearch(int fromIndex, int toIndex, DoubleToIntFunction comparison) {
        return PrimitiveListHelper.binarySearch(size(), this::get, fromIndex, toIndex, comparison);
    }

    @Override
    default IntRange indices() {
        return IntRange.of(0, size());
    }
}
