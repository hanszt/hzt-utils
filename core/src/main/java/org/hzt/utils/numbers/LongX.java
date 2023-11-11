package org.hzt.utils.numbers;

import org.hzt.utils.PreConditions;
import org.hzt.utils.Transformable;
import org.hzt.utils.comparables.ComparableX;
import org.hzt.utils.ranges.LongRange;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.LongSequence;

import java.io.Serial;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.LongPredicate;

@SuppressWarnings({"unused", "squid:S1448"})
public final class LongX extends Number implements NumberX<Long>, Transformable<LongX>, ComparableX<LongX> {

    @Serial
    private static final long serialVersionUID = 2;

    private final Long thisLong;

    private LongX(final Long thisLong) {
        this.thisLong = thisLong;
    }

    public static LongX of(final long aLong) {
        return new LongX(aLong);
    }

    public static LongX of(final Number number) {
        return new LongX(number.longValue());
    }

    public static String toString(final long i, final int radix) {
        return Long.toString(i, radix);
    }

    public static String toUnsignedString(final long i, final int radix) {
        return Long.toUnsignedString(i, radix);
    }

    public static String toHexString(final long i) {
        return Long.toHexString(i);
    }

    public static String toOctalString(final long i) {
        return Long.toOctalString(i);
    }

    public static String toBinaryString(final long i) {
        return Long.toBinaryString(i);
    }

    public static String toString(final long i) {
        return Long.toString(i);
    }

    public static String toUnsignedString(final long i) {
        return Long.toUnsignedString(i);
    }

    public static long parseLong(final String s, final int radix) throws NumberFormatException {
        return Long.parseLong(s, radix);
    }

    public static long parseLong(final String s) throws NumberFormatException {
        return Long.parseLong(s);
    }

    public static LongPredicate multipleOf(final long multiple) {
        return l -> l % multiple == 0;
    }

    public static long times(final long value1, final long value2) {
        return value1 * value2;
    }

    public boolean isMultipleOf(final long multiple) {
        return multipleOf(multiple).test(thisLong);
    }

    public static boolean isEven(final long i) {
        return multipleOf(2).test(i);
    }

    public boolean isEven() {
        return isEven(thisLong);
    }

    public static boolean isOdd(final long l) {
        return l % 2 != 0;
    }

    public boolean isOdd() {
        return isOdd(thisLong);
    }

    public static long parseUnsignedLong(final String s, final int radix) throws NumberFormatException {
        return Long.parseUnsignedLong(s, radix);
    }

    public static long parseUnsignedLong(final String s) throws NumberFormatException {
        return Long.parseUnsignedLong(s);
    }

    public static Long valueOf(final String s, final int radix) throws NumberFormatException {
        return Long.valueOf(s, radix);
    }

    public static Long valueOf(final String s) throws NumberFormatException {
        return Long.valueOf(s);
    }

    public static Long valueOf(final long l) {
        return l;
    }

    public static LongSequence fibonacciSequence() {
        return Sequence.iterate(new long[]{0, 1L}, longs -> new long[]{longs[1], longs[0] + longs[1]})
                .mapToLong(longs -> longs[0])
                .mapIndexed((index, fibNr) -> {
                    if (fibNr < 0) {
                        throw new NoSuchElementException("term n>=" + (index + 1) + " would yield value larger than Long.MAX_VALUE");
                    }
                    return fibNr;
                });
    }

    public static long nthFibonacciNumber(final int n) {
        PreConditions.require(n > 0, () -> "n must be greater than 0");
        return fibonacciSequence()
                .take(n)
                .reduce(0, (first, second) -> second);
    }

    public static Long decode(final String nm) throws NumberFormatException {
        return Long.decode(nm);
    }

    public LongRange until(final long l) {
        return LongRange.of(thisLong, l);
    }

    @Override
    public byte byteValue() {
        return thisLong.byteValue();
    }

    @Override
    public short shortValue() {
        return thisLong.shortValue();
    }

    @Override
    public int intValue() {
        return thisLong.intValue();
    }

    @Override
    public long longValue() {
        return thisLong;
    }

    @Override
    public float floatValue() {
        return thisLong.floatValue();
    }

    @Override
    public double doubleValue() {
        return thisLong.doubleValue();
    }

    @Override
    public String toString() {
        return thisLong.toString();
    }

    @Override
    public int hashCode() {
        return thisLong.hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final var longX = (LongX) o;
        return Objects.equals(thisLong, longX.thisLong);
    }

    public static Long getLong(final String nm) {
        return Long.getLong(nm);
    }

    public static Long getLong(final String nm, final long val) {
        return Long.getLong(nm, val);
    }

    public static Long getLong(final String nm, final Long val) {
        return Long.getLong(nm, val);
    }

    @SuppressWarnings("squid:S4351")
    public int compareTo(final Long anotherLong) {
        return thisLong.compareTo(anotherLong);
    }

    public static int compare(final long x, final long y) {
        return Long.compare(x, y);
    }

    public static int compareReversed(final long x, final long y) {
        return Long.compare(y, x);
    }

    public static int compareUnsigned(final long x, final long y) {
        return Long.compareUnsigned(x, y);
    }

    public static long divideUnsigned(final long dividend, final long divisor) {
        return Long.divideUnsigned(dividend, divisor);
    }

    public static long remainderUnsigned(final long dividend, final long divisor) {
        return Long.remainderUnsigned(dividend, divisor);
    }

    public static long highestOneBit(final long i) {
        return Long.highestOneBit(i);
    }

    public static long lowestOneBit(final long i) {
        return Long.lowestOneBit(i);
    }


    public static int numberOfLeadingZeros(final long i) {
        return Long.numberOfLeadingZeros(i);
    }


    public static int numberOfTrailingZeros(final long i) {
        return Long.numberOfTrailingZeros(i);
    }


    public static int bitCount(final long i) {
        return Long.bitCount(i);
    }

    public static long rotateLeft(final long i, final int distance) {
        return Long.rotateLeft(i, distance);
    }

    public static long rotateRight(final long i, final int distance) {
        return Long.rotateRight(i, distance);
    }

    public static long reverse(final long i) {
        return Long.reverse(i);
    }

    public static int signum(final long i) {
        return Long.signum(i);
    }


    public static long reverseBytes(final long i) {
        return Long.reverseBytes(i);
    }

    public static long sum(final long a, final long b) {
        return Long.sum(a, b);
    }

    public static long max(final long a, final long b) {
        return Long.max(a, b);
    }

    public static long min(final long a, final long b) {
        return Long.min(a, b);
    }

    @Override
    public LongX get() {
        return this;
    }

    @Override
    public Long getValue() {
        return thisLong;
    }

    @Override
    public int compareTo(final LongX o) {
        return thisLong.compareTo(o.thisLong);
    }
}
