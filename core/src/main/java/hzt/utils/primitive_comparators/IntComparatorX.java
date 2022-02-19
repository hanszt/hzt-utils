package hzt.utils.primitive_comparators;

import net.mintern.primitive.comparators.IntComparator;

public interface IntComparatorX extends IntComparator {

    static IntComparatorX comparing(IntComparator comparator) {
        return comparator::compare;
    }

    static IntComparatorX reverseOrder() {
        return (a, b) -> Integer.compare(b, a);
    }

    default IntComparatorX thenComparing(IntComparator comparator) {
        return comparator::compare;
    }

    default IntComparatorX reversed() {
        return (a, b) -> Integer.compare(b, a);
    }
}
