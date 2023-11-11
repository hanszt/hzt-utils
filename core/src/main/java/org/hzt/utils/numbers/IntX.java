package org.hzt.utils.numbers;

import org.hzt.utils.Transformable;
import org.hzt.utils.comparables.ComparableX;
import org.hzt.utils.progressions.IntProgression;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.primitives.IntSequence;

import java.util.Objects;
import java.util.function.IntPredicate;

@SuppressWarnings({"unused", "squid:S1448"})
public final class IntX extends Number implements NumberX<Integer>, Transformable<IntX>, ComparableX<IntX> {

    private static final long serialVersionUID = 20;

    private final Integer integer;

    private IntX(final int integer) {
        this.integer = integer;
    }

    public static IntX of(final Number number) {
        return new IntX(number.intValue());
    }

    public IntProgression downTo(final int target) {
        return target > integer ? IntProgression.empty() : IntProgression.closed(integer, target, -1);
    }

    public IntRange upTo(final int target) {
        return target < integer ? IntRange.empty() : IntRange.closed(integer, target);
    }

    public IntRange until(final int bound) {
        return IntRange.of(integer, bound);
    }

    public int coerceIn(final  int minimumValue, final int maximumValue) {
        return coerceIn(integer, minimumValue, maximumValue);
    }

    public static int coerceIn(final int integer, final int minimumValue, final int maximumValue) {
        if (minimumValue > maximumValue) {
            throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " +
                    maximumValue + " is less than minimum " + minimumValue);
        }
        if (integer < minimumValue) {
            return minimumValue;
        }
        return Math.min(integer, maximumValue);
    }

    public static String toString(final int i, final int radix) {
        return Integer.toString(i, radix);
    }

    public static String toUnsignedString(final int i, final int radix) {
        return Integer.toUnsignedString(i, radix);
    }

    public static String toHexString(final int i) {
        return Integer.toHexString(i);
    }

    public static String toOctalString(final int i) {
        return Integer.toOctalString(i);
    }

    public static String toBinaryString(final int i) {
        return Integer.toBinaryString(i);
    }

    public static String toString(final int i) {
        return Integer.toString(i);
    }

    public static String toUnsignedString(final int i) {
        return Integer.toUnsignedString(i);
    }

    public static int parseInt(final String s, final int radix) throws NumberFormatException {
        return Integer.parseInt(s, radix);
    }

    public static IntPredicate multipleOf(final int multiple) {
        return i -> i % multiple == 0;
    }

    public boolean isMultipleOf(final int multiple) {
        return multipleOf(multiple).test(integer);
    }

    public static boolean isEven(final int i) {
        return multipleOf(2).test(i);
    }

    public static int times(final int value1, final int value2) {
        return value1 * value2;
    }

    public boolean isEven() {
        return isEven(integer);
    }

    public static boolean isOdd(final int i) {
        return i % 2 != 0;
    }

    public boolean isOdd() {
        return isOdd(integer);
    }

    public static int parseInt(final String s) throws NumberFormatException {
        return Integer.parseInt(s);
    }

    public static char asChar(final int i) {
        return (char) i;
    }

    public static String asString(final int i) {
        return String.valueOf(asChar(i));
    }

    public static int parseUnsignedInt(final String s, final int radix) throws NumberFormatException {
        return Integer.parseUnsignedInt(s, radix);
    }

    public static int parseUnsignedInt(final String s) throws NumberFormatException {
        return Integer.parseUnsignedInt(s);
    }

    public static Integer valueOf(final String s, final int radix) throws NumberFormatException {
        return Integer.valueOf(s, radix);
    }

    public static Integer valueOf(final String s) throws NumberFormatException {
        return Integer.valueOf(s);
    }

    public static Integer valueOf(final int i) {
        return i;
    }

    public static IntSequence primeNrSequence() {
        return IntSequence.iterate(0, i -> i + (i < 3 ? 1 : 2))
                .takeWhile(i -> i >= 0)
                .filter(IntX::isPrimeNr);
    }

    public static boolean isPrimeNr(final long nrToCheck) {
        long counter = 0;
        for (var num = nrToCheck; num >= 1; num--) {
            if (nrToCheck % num == 0) {
                counter++;
            }
        }
        return counter == 2;
    }


    @Override
    public byte byteValue() {
        return integer.byteValue();
    }

    @Override
    public short shortValue() {
        return integer.shortValue();
    }

    public static IntX of(final int i) {
        return new IntX(i);
    }

    public IntX get() {
        return this;
    }

    @Override
    public int intValue() {
        return integer;
    }

    @Override
    public long longValue() {
        return integer.longValue();
    }

    @Override
    public float floatValue() {
        return integer.floatValue();
    }

    @Override
    public double doubleValue() {
        return integer.doubleValue();
    }

    @Override
    public String toString() {
        return integer.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final var intX = (IntX) o;
        return Objects.equals(integer, intX.integer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(integer);
    }

    public static Integer getInteger(final String nm) {
        return Integer.getInteger(nm);
    }

    public static Integer getInteger(final String nm, final int val) {
        return Integer.getInteger(nm, val);
    }

    public static Integer getInteger(final String nm, final Integer val) {
        return Integer.getInteger(nm, val);
    }

    public static Integer decode(final String nm) throws NumberFormatException {
        return Integer.decode(nm);
    }

    @SuppressWarnings("squid:S4351")
    public int compareTo(final Integer anotherInteger) {
        return integer.compareTo(anotherInteger);
    }

    public static int compare(final int x, final int y) {
        return Integer.compare(x, y);
    }

    public static int compareReversed(final int x, final int y) {
        return Integer.compare(y, x);
    }

    public static int compareUnsigned(final int x, final int y) {
        return Integer.compareUnsigned(x, y);
    }

    public static long toUnsignedLong(final int x) {
        return Integer.toUnsignedLong(x);
    }

    public static int divideUnsigned(final int dividend, final int divisor) {
        return Integer.divideUnsigned(dividend, divisor);
    }

    public static int remainderUnsigned(final int dividend, final int divisor) {
        return Integer.remainderUnsigned(dividend, divisor);
    }

    public static int highestOneBit(final int i) {
        return Integer.highestOneBit(i);
    }

    public static int lowestOneBit(final int i) {
        return Integer.lowestOneBit(i);
    }

    public static int numberOfLeadingZeros(final int i) {
        return Integer.numberOfLeadingZeros(i);
    }

    public static int numberOfTrailingZeros(final int i) {
        return Integer.numberOfTrailingZeros(i);
    }

    public static int bitCount(final int i) {
        return Integer.bitCount(i);
    }

    public static int rotateLeft(final int i, final int distance) {
        return Integer.rotateLeft(i, distance);
    }

    public static int rotateRight(final int i, final int distance) {
        return Integer.rotateRight(i, distance);
    }

    public static int reverse(final int i) {
        return Integer.reverse(i);
    }

    public static int signum(final int i) {
        return Integer.signum(i);
    }

    public static int reverseBytes(final int i) {
        return Integer.reverseBytes(i);
    }

    public static int sum(final int a, final int b) {
        return Integer.sum(a, b);
    }

    public static int max(final int a, final int b) {
        return Integer.max(a, b);
    }

    public static int min(final int a, final int b) {
        return Integer.min(a, b);
    }

    @Override
    public Integer getValue() {
        return integer;
    }

    @Override
    public int compareTo(final IntX o) {
        return integer.compareTo(o.integer);
    }
}
