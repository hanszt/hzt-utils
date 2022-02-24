package hzt.utils.primitive_comparators;

public interface IntComparator {

    int compare(int var1, int var2);

    static IntComparator comparing(IntComparator comparator) {
        return comparator;
    }

    static IntComparator reverseOrder() {
        return (a, b) -> Integer.compare(b, a);
    }

    default IntComparator thenComparing(IntComparator comparator) {
        return comparator;
    }

    default IntComparator reversed() {
        return (a, b) -> Integer.compare(b, a);
    }
}
