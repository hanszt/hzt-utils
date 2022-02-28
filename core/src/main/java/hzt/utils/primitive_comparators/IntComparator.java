package hzt.utils.primitive_comparators;

import java.util.Comparator;

@FunctionalInterface
public interface IntComparator extends Comparator<Integer> {

    int compareInt(int i1, int i2);

    @Override
    default int compare(Integer i1, Integer i2) {
        return compareInt(i1, i2);
    }

    static IntComparator comparing(IntComparator comparator) {
        return comparator;
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
