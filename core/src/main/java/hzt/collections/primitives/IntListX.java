package hzt.collections.primitives;

import hzt.arrays.primitves.PrimitiveSort;
import hzt.collections.ListX;
import hzt.iterables.primitives.PrimitiveSortable;
import hzt.numbers.IntX;
import hzt.utils.primitive_comparators.IntComparator;

import java.util.Arrays;

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

    default boolean contains(int i) {
        return indexOf(i) >= 0;
    }

    int get(int index);

    int indexOf(int i);

    int lastIndexOf(int i);

    @Override
    int[] toArray();

    @Override
    default ListX<Integer> boxed() {
        return asSequence().boxed().toListX();
    }

    @Override
    default IntListX sorted() {
        final var array = toArray();
        Arrays.sort(array);
        return IntListX.of(array);
    }

    @Override
    default IntListX sorted(IntComparator comparator) {
        final var array = toArray();
        PrimitiveSort.sort(array, comparator);
        return IntListX.of(array);
    }

    @Override
    default IntListX sortedDescending() {
        return sorted(IntX::compareReversed);
    }
}
