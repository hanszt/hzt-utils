package hzt.utils.primitive_comparators;

public interface DoubleComparator {

    int compare(double var1, double var3);

    static DoubleComparator comparing(DoubleComparator comparator) {
        return comparator::compare;
    }

    static DoubleComparator reverseOrder() {
        return (a, b) -> Double.compare(b, a);
    }

    default DoubleComparator thenComparing(DoubleComparator comparator) {
        return comparator::compare;
    }

    default DoubleComparator reversed() {
        return (a, b) -> Double.compare(b, a);
    }
}
