package org.hzt.utils.numbers;

import org.hzt.utils.Transformable;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.comparables.ComparableX;
import org.hzt.utils.sequences.Sequence;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@SuppressWarnings("unused")
public final class BigDecimalX extends BigDecimal implements NumberX<BigDecimal>, Transformable<BigDecimalX>, ComparableX<BigDecimal> {

    private static final long serialVersionUID = 234;

    public static final BigDecimalX ZERO = BigDecimalX.of(BigDecimal.ZERO);
    public static final BigDecimalX ONE = BigDecimalX.of(BigDecimal.ONE);

    private BigDecimalX(final char[] in, final int offset, final int len) {
        super(in, offset, len);
    }

    private BigDecimalX(final String val) {
        super(val);
    }

    private BigDecimalX(final String val, final MathContext mc) {
        super(val, mc);
    }

    public static BigDecimalX of(final BigDecimal bigDecimal) {
        return new BigDecimalX(bigDecimal.toPlainString());
    }

    public static BigDecimalX of(final String s) {
        return new BigDecimalX(s);
    }

    public static BigDecimalX of(final String s, final MathContext mathContext) {
        return new BigDecimalX(s, mathContext);
    }

    public static BigDecimalX of(final Number n) {
        return new BigDecimalX(n.toString());
    }

    public static BigDecimalX of(final int s) {
        return BigDecimalX.of(BigDecimal.valueOf(s));
    }

    public static BigDecimalX of(final long s) {
        return BigDecimalX.of(BigDecimal.valueOf(s));
    }

    public static BigDecimalX of(final double s) {
        return BigDecimalX.of(BigDecimal.valueOf(s));
    }

    @Override
    public BigDecimalX add(final BigDecimal augend) {
        return BigDecimalX.of(super.add(augend));
    }

    @Override
    public BigDecimalX add(final BigDecimal augend, final MathContext mc) {
        return of(super.add(augend, mc));
    }

    @Override
    public BigDecimalX subtract(final BigDecimal subtrahend) {
        return of(super.subtract(subtrahend));
    }

    @Override
    public BigDecimalX subtract(final BigDecimal subtrahend, final MathContext mc) {
        return of(super.subtract(subtrahend, mc));
    }

    @Override
    public BigDecimalX multiply(final BigDecimal multiplicand) {
        return of(super.multiply(multiplicand));
    }

    @Override
    public BigDecimalX multiply(final BigDecimal multiplicand, final MathContext mc) {
        return of(super.multiply(multiplicand, mc));
    }

    @Override
    public BigDecimalX divide(final BigDecimal divisor, final int scale, final RoundingMode roundingMode) {
        return of(super.divide(divisor, scale, roundingMode));
    }

    @Override
    public BigDecimalX divide(final BigDecimal divisor, final RoundingMode roundingMode) {
        return of(super.divide(divisor, roundingMode));
    }

    @Override
    public BigDecimalX divide(final BigDecimal divisor, final MathContext mc) {
        return of(super.divide(divisor, mc));
    }

    @Override
    public BigDecimalX divideToIntegralValue(final BigDecimal divisor) {
        return of(super.divideToIntegralValue(divisor));
    }

    @Override
    public BigDecimalX divideToIntegralValue(final BigDecimal divisor, final MathContext mc) {
        return of(super.divideToIntegralValue(divisor, mc));
    }

    @Override
    public BigDecimalX remainder(final BigDecimal divisor) {
        return of(super.remainder(divisor));
    }

    @Override
    public BigDecimalX remainder(final BigDecimal divisor, final MathContext mc) {
        return of(super.remainder(divisor, mc));
    }

    public ListX<BigDecimalX> divideAndRemainderX(final BigDecimal divisor) {
        return Sequence.of(super.divideAndRemainder(divisor)).map(BigDecimalX::of).toListX();
    }

    public ListX<BigDecimalX> divideAndRemainderX(final BigDecimal divisor, final MathContext mc) {
        return Sequence.of(super.divideAndRemainder(divisor, mc)).map(BigDecimalX::of).toListX();
    }

    @Override
    public BigDecimalX pow(final int n) {
        return of(super.pow(n));
    }

    @Override
    public BigDecimalX pow(final int n, final MathContext mc) {
        return of(super.pow(n, mc));
    }

    @Override
    public BigDecimalX abs() {
        return of(super.abs());
    }

    @Override
    public BigDecimalX abs(final MathContext mc) {
        return of(super.abs(mc));
    }

    @Override
    public BigDecimalX negate() {
        return of(super.negate());
    }

    @Override
    public BigDecimalX negate(final MathContext mc) {
        return of(super.negate(mc));
    }

    @Override
    public BigDecimalX plus() {
        return of(super.plus());
    }

    @Override
    public BigDecimalX plus(final MathContext mc) {
        return of(super.plus(mc));
    }

    @Override
    public BigDecimalX round(final MathContext mc) {
        return of(super.round(mc));
    }

    @Override
    public BigDecimalX setScale(final int newScale, final RoundingMode roundingMode) {
        return of(super.setScale(newScale, roundingMode));
    }

    @Override
    public BigDecimalX movePointLeft(final int n) {
        return of(super.movePointLeft(n));
    }

    @Override
    public BigDecimalX movePointRight(final int n) {
        return of(super.movePointRight(n));
    }

    @Override
    public BigDecimalX scaleByPowerOfTen(final int n) {
        return of(super.scaleByPowerOfTen(n));
    }

    @Override
    public BigDecimalX stripTrailingZeros() {
        return of(super.stripTrailingZeros());
    }

    @Override
    public BigDecimalX sqrt(final MathContext mc) {
        return of(super.sqrt(mc));
    }

    @Override
    public BigDecimalX min(final BigDecimal val) {
        return of(super.min(val));
    }

    @Override
    public BigDecimalX max(final BigDecimal val) {
        return of(super.max(val));
    }

    @Override
    public BigDecimalX ulp() {
        return of(super.ulp());
    }

    @Override
    public BigDecimalX get() {
        return this;
    }

    @Override
    public BigDecimal getValue() {
        return this;
    }
}
