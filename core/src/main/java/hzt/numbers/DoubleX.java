package hzt.numbers;

import hzt.function.Transformable;

public final class DoubleX extends Number implements NumberX, Transformable<DoubleX> {

    private final Double aDouble;

    private DoubleX(double aDouble) {
        this.aDouble = aDouble;
    }

    public static String toString(double d) {
        return Double.toString(d);
    }

    public static String toHexString(double d) {
        return Double.toHexString(d);
    }

    public static Double valueOf(String s) throws NumberFormatException {
        return Double.valueOf(s);
    }

    
    public static Double valueOf(double d) {
        return d;
    }

    public static double parseDouble(String s) throws NumberFormatException {
        return Double.parseDouble(s);
    }

    public static boolean isNaN(double v) {
        return Double.isNaN(v);
    }

    public static boolean isInfinite(double v) {
        return Double.isInfinite(v);
    }

    public static boolean isFinite(double d) {
        return Double.isFinite(d);
    }

    public boolean isNaN() {
        return aDouble.isNaN();
    }

    public boolean isInfinite() {
        return aDouble.isInfinite();
    }

    @Override
    public String toString() {
        return aDouble.toString();
    }

    @Override
    public byte byteValue() {
        return aDouble.byteValue();
    }

    @Override
    public short shortValue() {
        return aDouble.shortValue();
    }

    public static DoubleX of(double aDouble) {
        return new DoubleX(aDouble);
    }

    @Override
    public int intValue() {
        return aDouble.intValue();
    }

    @Override
    public long longValue() {
        return aDouble.longValue();
    }

    @Override
    public float floatValue() {
        return aDouble.floatValue();
    }

    
    @Override
    public double doubleValue() {
        return aDouble;
    }

    @Override
    public int hashCode() {
        return aDouble.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DoubleX doubleX = (DoubleX) o;
        return aDouble.equals(doubleX.aDouble);
    }

    public static long doubleToLongBits(double value) {
        return Double.doubleToLongBits(value);
    }

    
    public static long doubleToRawLongBits(double value) {
        return Double.doubleToRawLongBits(value);
    }

    
    public static double longBitsToDouble(long bits) {
        return Double.longBitsToDouble(bits);
    }

    public int compareTo(Double anotherDouble) {
        return aDouble.compareTo(anotherDouble);
    }

    public static int compare(double d1, double d2) {
        return Double.compare(d1, d2);
    }

    public static double sum(double a, double b) {
        return Double.sum(a, b);
    }

    public static double max(double a, double b) {
        return Double.max(a, b);
    }

    public static double min(double a, double b) {
        return Double.min(a, b);
    }

    public IntX toIntX() {
        return IntX.of(intValue());
    }

    @Override
    public DoubleX get() {
        return this;
    }
}
