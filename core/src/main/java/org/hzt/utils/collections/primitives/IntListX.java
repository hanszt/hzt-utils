package org.hzt.utils.collections.primitives;

import org.hzt.utils.arrays.primitves.PrimitiveSort;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterables.primitives.PrimitiveSortable;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.primitive_comparators.IntComparator;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.OptionalInt;
import java.util.function.Consumer;

public interface IntListX extends IntCollection, PrimitiveSortable<IntComparator> {

    static IntListX empty() {
        return new IntArrayList();
    }

    static IntListX of(Iterable<Integer> iterable) {
        return new IntArrayList(iterable);
    }

    static IntListX of(IntListX intListX) {
        return new IntArrayList(intListX);
    }

    static IntListX of(int... array) {
        return new IntArrayList(array);
    }

    static IntListX build(Consumer<IntMutableListX> factory) {
        final IntMutableListX listX = IntMutableListX.empty();
        factory.accept(listX);
        return listX;
    }

    default boolean contains(int i) {
        return indexOf(i) >= 0;
    }

    int get(int index);

    int indexOf(int i);

    int lastIndexOf(int i);

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
    default IntListX sorted() {
        final int[] array = toArray();
        Arrays.sort(array);
        return IntListX.of(array);
    }

    @Override
    default IntListX sorted(IntComparator comparator) {
        final int[] array = toArray();
        PrimitiveSort.sort(array, comparator);
        return IntListX.of(array);
    }

    @Override
    default IntListX sortedDescending() {
        return sorted(IntX::compareReversed);
    }

    IntListX shuffled();
}
