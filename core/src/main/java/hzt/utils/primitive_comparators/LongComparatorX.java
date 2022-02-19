package hzt.utils.primitive_comparators;

import net.mintern.primitive.comparators.LongComparator;

public interface LongComparatorX extends LongComparator {

    static LongComparatorX comparing(LongComparator comparator) {
        return comparator::compare;
    }

    static LongComparatorX reverseOrder() {
        return (a, b) -> Long.compare(b, a);
    }

    default LongComparatorX thenComparing(LongComparator comparator) {
        return comparator::compare;
    }

    default LongComparatorX reversed() {
        return (a, b) -> Long.compare(b, a);
    }
}
