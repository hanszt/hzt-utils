package hzt.utils.primitive_comparators;

public interface LongComparator {

    int compare(long var1, long var3);

    static LongComparator comparing(LongComparator comparator) {
        return comparator;
    }

    static LongComparator reverseOrder() {
        return (a, b) -> Long.compare(b, a);
    }

    default LongComparator thenComparing(LongComparator comparator) {
        return comparator;
    }

    default LongComparator reversed() {
        return (a, b) -> Long.compare(b, a);
    }
}
