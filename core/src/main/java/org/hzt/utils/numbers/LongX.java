package org.hzt.utils.numbers;

import org.hzt.utils.ranges.LongRange;
import org.hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.LongPredicate;

@SuppressWarnings("unused")
public final class LongX extends Number implements NumberX<Long>, Transformable<LongX> {

    private static final long serialVersionUID = 2;

    private final Long thisLong;

    private LongX(Long thisLong) {
        this.thisLong = thisLong;
    }

    public static LongX of(long aLong) {
        return new LongX(aLong);
    }

    public static LongX of(Number number) {
        return new LongX(number.longValue());
    }

    public static String toString(long i, int radix) {
        return Long.toString(i, radix);
    }

    public static String toUnsignedString(long i, int radix) {
        return Long.toUnsignedString(i, radix);
    }

    public static String toHexString(long i) {
        return Long.toHexString(i);
    }

    public static String toOctalString(long i) {
        return Long.toOctalString(i);
    }

    public static String toBinaryString(long i) {
        return Long.toBinaryString(i);
    }

    public static String toString(long i) {
        return Long.toString(i);
    }

    public static String toUnsignedString(long i) {
        return Long.toUnsignedString(i);
    }

    public static long parseLong(String s, int radix) throws NumberFormatException {
        return Long.parseLong(s, radix);
    }

    public static long parseLong(String s) throws NumberFormatException {
        return Long.parseLong(s);
    }

    public static LongPredicate multipleOf(long multiple) {
        return l -> l % multiple == 0;
    }

    public boolean isMultipleOf(long multiple) {
        return multipleOf(multiple).test(thisLong);
    }

    public static boolean isEven(long i) {
        return multipleOf(2).test(i);
    }

    public boolean isEven() {
        return isEven(thisLong);
    }

    public static boolean isOdd(long l) {
        return l % 2 != 0;
    }

    public boolean isOdd() {
        return isOdd(thisLong);
    }

    public static long parseUnsignedLong(String s, int radix) throws NumberFormatException {
        return Long.parseUnsignedLong(s, radix);
    }

    public static long parseUnsignedLong(String s) throws NumberFormatException {
        return Long.parseUnsignedLong(s);
    }

    public static Long valueOf(String s, int radix) throws NumberFormatException {
        return Long.valueOf(s, radix);
    }

    public static Long valueOf(String s) throws NumberFormatException {
        return Long.valueOf(s);
    }


    public static Long valueOf(long l) {
        return l;
    }

    public static Long decode(String nm) throws NumberFormatException {
        return Long.decode(nm);
    }

    public LongRange until(long l) {
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LongX longX = (LongX) o;
        return Objects.equals(thisLong, longX.thisLong);
    }

    public static Long getLong(String nm) {
        return Long.getLong(nm);
    }

    public static Long getLong(String nm, long val) {
        return Long.getLong(nm, val);
    }

    public static Long getLong(String nm, Long val) {
        return Long.getLong(nm, val);
    }

    public int compareTo(Long anotherLong) {
        return thisLong.compareTo(anotherLong);
    }

    public static int compare(long x, long y) {
        return Long.compare(x, y);
    }

    public static int compareReversed(long x, long y) {
        return Long.compare(y, x);
    }

    public static int compareUnsigned(long x, long y) {
        return Long.compareUnsigned(x, y);
    }

    public static long divideUnsigned(long dividend, long divisor) {
        return Long.divideUnsigned(dividend, divisor);
    }

    public static long remainderUnsigned(long dividend, long divisor) {
        return Long.remainderUnsigned(dividend, divisor);
    }

    public static long highestOneBit(long i) {
        return Long.highestOneBit(i);
    }

    public static long lowestOneBit(long i) {
        return Long.lowestOneBit(i);
    }


    public static int numberOfLeadingZeros(long i) {
        return Long.numberOfLeadingZeros(i);
    }


    public static int numberOfTrailingZeros(long i) {
        return Long.numberOfTrailingZeros(i);
    }


    public static int bitCount(long i) {
        return Long.bitCount(i);
    }

    public static long rotateLeft(long i, int distance) {
        return Long.rotateLeft(i, distance);
    }

    public static long rotateRight(long i, int distance) {
        return Long.rotateRight(i, distance);
    }

    public static long reverse(long i) {
        return Long.reverse(i);
    }

    public static int signum(long i) {
        return Long.signum(i);
    }


    public static long reverseBytes(long i) {
        return Long.reverseBytes(i);
    }

    public static long sum(long a, long b) {
        return Long.sum(a, b);
    }

    public static long max(long a, long b) {
        return Long.max(a, b);
    }

    public static long min(long a, long b) {
        return Long.min(a, b);
    }

    @Override
    public @NotNull LongX get() {
        return this;
    }

    @Override
    public @NotNull Long getValue() {
        return thisLong;
    }
}
