package org.hzt.utils.primitive_comparators;

import java.util.Comparator;
import java.util.function.LongUnaryOperator;

import static java.lang.System.Logger.Level.WARNING;

@FunctionalInterface
public interface LongComparator extends Comparator<Long>, PrimitiveComparator {

    int compareLong(long l1, long l2);

    @Override
    default int compare(Long l1, Long l2) {
        System.getLogger(getClass().getSimpleName()).log(WARNING, "Use `compareLong` instead");
        return compareLong(l1, l2);
    }

    static LongComparator comparing(LongUnaryOperator selector) {
        return (l1, l2) -> Long.compare(selector.applyAsLong(l1), selector.applyAsLong(l2));
    }

    static LongComparator naturalOrder() {
        return Long::compare;
    }

    static LongComparator reverseOrder() {
        return (l1, l2) -> Long.compare(l2, l1);
    }

    default LongComparator thenComparing(LongComparator comparator) {
        return comparator;
    }

    @Override
    default LongComparator reversed() {
        return reverseOrder();
    }
}
