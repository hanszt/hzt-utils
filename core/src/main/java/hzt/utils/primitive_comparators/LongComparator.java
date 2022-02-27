package hzt.utils.primitive_comparators;

import java.util.Comparator;

@FunctionalInterface
public interface LongComparator extends Comparator<Long> {

    int compareLong(long l1, long l2);

    @Override
    default int compare(Long l1, Long l2) {
        return compareLong(l1, l2);
    }

    static LongComparator comparing(LongComparator comparator) {
        return comparator;
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
