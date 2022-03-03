package org.hzt.utils.primitive_comparators;

import java.util.Comparator;
import java.util.function.IntUnaryOperator;

@FunctionalInterface
public interface IntComparator extends Comparator<Integer> {

    int compareInt(int i1, int i2);

    @Override
    default int compare(Integer i1, Integer i2) {
        return compareInt(i1, i2);
    }

    static IntComparator comparing(IntUnaryOperator selector) {
        return (i1, i2) -> Integer.compare(selector.applyAsInt(i1), selector.applyAsInt(i2));
    }

    static IntComparator naturalOrder() {
        return Integer::compare;
    }

    static IntComparator reverseOrder() {
        return (i1, i2) -> Integer.compare(i2, i1);
    }

    default IntComparator thenComparing(IntComparator comparator) {
        return comparator;
    }

    @Override
    default IntComparator reversed() {
        return reverseOrder();
    }
}
