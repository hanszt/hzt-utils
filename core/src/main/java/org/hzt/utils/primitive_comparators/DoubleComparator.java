package org.hzt.utils.primitive_comparators;

import java.util.function.DoubleUnaryOperator;

@FunctionalInterface
public interface DoubleComparator extends PrimitiveComparator {

    int compare(double d1, double d2);

    static DoubleComparator comparing(DoubleUnaryOperator selector) {
        return (d1, d2) -> Double.compare(selector.applyAsDouble(d1), selector.applyAsDouble(d2));
    }

    static DoubleComparator naturalOrder() {
        return Double::compare;
    }

    static DoubleComparator reverseOrder() {
        return (d1, d2) -> Double.compare(d2, d1);
    }

    default DoubleComparator thenComparing(DoubleComparator comparator) {
        return comparator;
    }

    default DoubleComparator reversed() {
        return reverseOrder();
    }
}
