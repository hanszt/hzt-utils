package hzt.utils.primitive_comparators;

import net.mintern.primitive.comparators.DoubleComparator;

public interface DoubleComparatorX extends DoubleComparator {

    static DoubleComparatorX comparing(DoubleComparator comparator) {
        return comparator::compare;
    }

    static DoubleComparatorX reverseOrder() {
        return (a, b) -> Double.compare(b, a);
    }

    default DoubleComparatorX thenComparing(DoubleComparator comparator) {
        return comparator::compare;
    }

    default DoubleComparatorX reversed() {
        return (a, b) -> Double.compare(b, a);
    }
}
