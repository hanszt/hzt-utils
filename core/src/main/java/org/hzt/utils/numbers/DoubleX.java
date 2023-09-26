package org.hzt.utils.numbers;

import org.hzt.utils.Transformable;
import org.hzt.utils.comparables.ComparableX;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.regex.Pattern;

@SuppressWarnings({"unused", "squid:S1448"})
public final class DoubleX extends Number implements NumberX<Double>, Transformable<DoubleX>, ComparableX<DoubleX> {

    public static final double GOLDEN_RATIO = (1 + Math.sqrt(5)) / 2.0;

    private static final long serialVersionUID = 45;

    private static final String DIGIT = "(\\d+)";
    private static final String HEX_DIGITS = "(\\p{XDigit}+)";
    // an exponent is 'e' or 'E' followed by an optionally
    // signed decimal integer.
    private static final String EXP = "[eE][+-]?" + DIGIT;
    private static final String FP_REGEX =
            ("[\\x00-\\x20]*" +  // Optional leading "whitespace"
                    "[+-]?(" + // Optional sign character
                    "NaN|" +           // "NaN" string
                    "Infinity|" +      // "Infinity" string
                    // A decimal floating-point string representing a finite positive
                    // number without a leading sign has at most five basic pieces:
                    // Digits . Digits ExponentPart FloatTypeSuffix
                    //
                    // Since this method allows integer-only strings as input
                    // in addition to strings of floating-point literals, the
                    // two sub-patterns below are simplifications of the grammar
                    // productions from section 3.10.2 of
                    // The Java Language Specification.
                    // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
                    "(((" + DIGIT + "(\\.)?(" + DIGIT + "?)(" + EXP + ")?)|" +
                    // . Digits ExponentPart_opt FloatTypeSuffix_opt
                    "(\\." + DIGIT + "(" + EXP + ")?)|" +
                    // Hexadecimal strings
                    "((" +
                    // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
                    "(0[xX]" + HEX_DIGITS + "(\\.)?)|" +
                    // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
                    "(0[xX]" + HEX_DIGITS + "?(\\.)" + HEX_DIGITS + ")" +
                    ")[pP][+-]?" + DIGIT + "))" +
                    "[fFdD]?))" +
                    "[\\x00-\\x20]*");// Optional trailing "whitespace"

    private static final Pattern DOUBLE_PATTERN = Pattern.compile(FP_REGEX);

    private final Double thisDouble;

    private DoubleX(final double thisDouble) {
        this.thisDouble = thisDouble;
    }

    public static DoubleX of(final double aDouble) {
        return new DoubleX(aDouble);
    }

    public static DoubleX of(final Number number) {
        return new DoubleX(number.doubleValue());
    }

    public static DoubleX of(final String s) throws NumberFormatException {
        return DoubleX.of(Double.valueOf(s));
    }

    public static double parseDouble(final String s) throws NumberFormatException {
        return Double.parseDouble(s);
    }

    public static boolean isNaN(final double v) {
        return Double.isNaN(v);
    }

    public static boolean isInfinite(final double v) {
        return Double.isInfinite(v);
    }

    public static boolean isParsableDouble(final CharSequence charSequence) {
        return DOUBLE_PATTERN.matcher(charSequence).matches();
    }

    public static boolean isFinite(final double d) {
        return Double.isFinite(d);
    }

    public static String toRoundedString(final double d) {
        return String.format("%.2f", d);
    }

    public static String toRoundedString(final double d, final int scale) {
        return String.format(String.format("%%.%df", scale), d);
    }

    public static String toRoundedString(final double d, final int scale, final Locale locale) {
        final var defaultLocale = Locale.getDefault();
        Locale.setDefault(locale);
        final var format = String.format(String.format("%%.%df", scale), d);
        Locale.setDefault(defaultLocale);
        return format;
    }

    public static double times(final double value1, final double value2) {
        return value1 * value2;
    }

    public String toHexString() {
        return Double.toHexString(thisDouble);
    }

    public boolean isNaN() {
        return thisDouble.isNaN();
    }

    public boolean isInfinite() {
        return thisDouble.isInfinite();
    }

    @Override
    public String toString() {
        return thisDouble.toString();
    }

    @Override
    public byte byteValue() {
        return thisDouble.byteValue();
    }

    @Override
    public short shortValue() {
        return thisDouble.shortValue();
    }

    @Override
    public int intValue() {
        return thisDouble.intValue();
    }

    @Override
    public long longValue() {
        return thisDouble.longValue();
    }

    @Override
    public float floatValue() {
        return thisDouble.floatValue();
    }


    @Override
    public double doubleValue() {
        return thisDouble;
    }

    @Override
    public int hashCode() {
        return thisDouble.hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final var doubleX = (DoubleX) o;
        return Double.compare(thisDouble, doubleX.thisDouble) == 0;
    }

    public static long doubleToLongBits(final double value) {
        return Double.doubleToLongBits(value);
    }


    public static long doubleToRawLongBits(final double value) {
        return Double.doubleToRawLongBits(value);
    }


    public static double longBitsToDouble(final long bits) {
        return Double.longBitsToDouble(bits);
    }

    @SuppressWarnings("squid:S4351")
    public int compareTo(final Double anotherDouble) {
        return thisDouble.compareTo(anotherDouble);
    }

    public static int compare(final double d1, final double d2) {
        return Double.compare(d1, d2);
    }

    public static int compareReversed(final double d1, final double d2) {
        return Double.compare(d2, d1);
    }

    public static double sum(final double a, final double b) {
        return Double.sum(a, b);
    }

    public static double max(final double a, final double b) {
        return Double.max(a, b);
    }

    public static double min(final double a, final double b) {
        return Double.min(a, b);
    }

    @Override
    public @NotNull DoubleX get() {
        return this;
    }

    @Override
    public @NotNull Double getValue() {
        return thisDouble;
    }

    @Override
    public int compareTo(@NotNull final DoubleX o) {
        return thisDouble.compareTo(o.thisDouble);
    }
}
