package org.hzt.utils.collections.primitives;

import org.hzt.utils.arrays.ArraysX;
import org.hzt.utils.collections.BinarySearchable;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterables.primitives.PrimitiveSortable;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.primitive_comparators.DoubleComparator;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.primitives.DoubleSequence;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.DoublePredicate;
import java.util.function.DoubleToIntFunction;

public interface DoubleList extends DoubleCollection,
        PrimitiveList<PrimitiveListIterator.OfDouble>,
        PrimitiveSortable<DoubleComparator>,
        BinarySearchable<DoubleToIntFunction> {

    static DoubleList empty() {
        return new DoubleImmutableList();
    }

    static DoubleList of(final double... array) {
        return new DoubleImmutableList(array);
    }

    static DoubleList of(final Iterable<Double> iterable) {
        return DoubleSequence.of(iterable).toList();
    }

    static DoubleList copyOf(final DoubleCollection doubleCollection) {
        return new DoubleImmutableList(doubleCollection);
    }

    static DoubleList build(final Consumer<? super DoubleMutableList> factory) {
        final DoubleMutableList mutableList = DoubleMutableList.empty();
        factory.accept(mutableList);
        return copyOf(mutableList);
    }

    default boolean contains(final double value) {
        return indexOf(value) >= 0;
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
    default OptionalDouble findLast() {
        final int lastIndex = lastIndex();
        return lastIndex < 0 ? OptionalDouble.empty() : OptionalDouble.of(get(lastIndex));
    }

    @Override
    default OptionalDouble findLast(final DoublePredicate predicate) {
        final PrimitiveListIterator.OfDouble iterator = listIterator(lastIndex());
        while (iterator.hasPrevious()) {
            final double previousDouble = iterator.previousDouble();
            if (predicate.test(previousDouble)) {
                return OptionalDouble.of(previousDouble);
            }
        }
        return OptionalDouble.empty();
    }

    default double random(final Random random) {
        return findRandom(random).orElseThrow(NoSuchElementException::new);
    }

    OptionalDouble findRandom(Random random);

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
    default DoubleList sorted(final DoubleComparator comparator) {
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

    DoubleList shuffled(Random random);

    /**
     * @see BinarySearchable#binarySearch(int, int, Object)
     * @see java.util.Arrays#binarySearch(double[], double)
     */
    @Override
    default int binarySearch(final int fromIndex, final int toIndex, final DoubleToIntFunction comparison) {
        return BinarySearchable.binarySearch(size(), fromIndex, toIndex, mid -> comparison.applyAsInt(get(mid)));
    }

    default int binarySearch(final double valueToSearch) {
        return binarySearch(0, size(), e -> Double.compare(e, valueToSearch));
    }

    @Override
    default IntRange indices() {
        return IntRange.of(0, size());
    }
}
