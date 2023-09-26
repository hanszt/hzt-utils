package org.hzt.utils.primitive_comparators;

import java.util.function.LongUnaryOperator;

@FunctionalInterface
public interface LongComparator extends PrimitiveComparator {

    int compare(long l1, long l2);

    static LongComparator comparing(final LongUnaryOperator selector) {
        return (l1, l2) -> Long.compare(selector.applyAsLong(l1), selector.applyAsLong(l2));
    }

    static LongComparator naturalOrder() {
        return Long::compare;
    }

    static LongComparator reverseOrder() {
        return (l1, l2) -> Long.compare(l2, l1);
    }

    default LongComparator thenComparing(final LongComparator comparator) {
        return comparator;
    }
}
