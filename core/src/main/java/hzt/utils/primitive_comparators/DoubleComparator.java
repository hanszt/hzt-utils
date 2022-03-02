package hzt.utils.primitive_comparators;

import java.util.Comparator;
import java.util.function.DoubleUnaryOperator;

@FunctionalInterface
public interface DoubleComparator extends Comparator<Double> {

    int compareDouble(double d1, double d2);

    @Override
    default int compare(Double d1, Double d2) {
        return compareDouble(d1, d2);
    }

    static DoubleComparator comparing(DoubleUnaryOperator selector) {
        return (d1, d2) -> Double.compare(selector.applyAsDouble(d1), selector.applyAsDouble(d2));
    }

    static DoubleComparator naturalOrder() {
        return Double::compare;
    }

    static DoubleComparator reverseOrder() {
        return (d1, d2) -> Double.compare(d2, d1);
    }

    default DoubleComparator thenComparing(DoubleComparator comparator) {
        return comparator;
    }

    @Override
    default DoubleComparator reversed() {
        return reverseOrder();
    }
}
