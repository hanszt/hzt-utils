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

    private BigDecimalX(char[] in, int offset, int len) {
        super(in, offset, len);
    }

    private BigDecimalX(String val) {
        super(val);
    }

    private BigDecimalX(String val, MathContext mc) {
        super(val, mc);
    }

    public static BigDecimalX of(BigDecimal bigDecimal) {
        return new BigDecimalX(bigDecimal.toPlainString());
    }

    public static BigDecimalX of(String s) {
        return new BigDecimalX(s);
    }

    public static BigDecimalX of(String s, MathContext mathContext) {
        return new BigDecimalX(s, mathContext);
    }

    public static BigDecimalX of(Number n) {
        return new BigDecimalX(n.toString());
    }

    public static BigDecimalX of(int s) {
        return BigDecimalX.of(BigDecimal.valueOf(s));
    }

    public static BigDecimalX of(long s) {
        return BigDecimalX.of(BigDecimal.valueOf(s));
    }

    public static BigDecimalX of(double s) {
        return BigDecimalX.of(BigDecimal.valueOf(s));
    }

    @Override
    public BigDecimalX add(BigDecimal augend) {
        return BigDecimalX.of(super.add(augend));
    }

    @Override
    public BigDecimalX add(BigDecimal augend, MathContext mc) {
        return of(super.add(augend, mc));
    }

    @Override
    public BigDecimalX subtract(BigDecimal subtrahend) {
        return of(super.subtract(subtrahend));
    }

    @Override
    public BigDecimalX subtract(BigDecimal subtrahend, MathContext mc) {
        return of(super.subtract(subtrahend, mc));
    }

    @Override
    public BigDecimalX multiply(BigDecimal multiplicand) {
        return of(super.multiply(multiplicand));
    }

    @Override
    public BigDecimalX multiply(BigDecimal multiplicand, MathContext mc) {
        return of(super.multiply(multiplicand, mc));
    }

    @Override
    public BigDecimalX divide(BigDecimal divisor, int scale, RoundingMode roundingMode) {
        return of(super.divide(divisor, scale, roundingMode));
    }

    @Override
    public BigDecimalX divide(BigDecimal divisor, RoundingMode roundingMode) {
        return of(super.divide(divisor, roundingMode));
    }

    @Override
    public BigDecimalX divide(BigDecimal divisor, MathContext mc) {
        return of(super.divide(divisor, mc));
    }

    @Override
    public BigDecimalX divideToIntegralValue(BigDecimal divisor) {
        return of(super.divideToIntegralValue(divisor));
    }

    @Override
    public BigDecimalX divideToIntegralValue(BigDecimal divisor, MathContext mc) {
        return of(super.divideToIntegralValue(divisor, mc));
    }

    @Override
    public BigDecimalX remainder(BigDecimal divisor) {
        return of(super.remainder(divisor));
    }

    @Override
    public BigDecimalX remainder(BigDecimal divisor, MathContext mc) {
        return of(super.remainder(divisor, mc));
    }

    public ListX<BigDecimalX> divideAndRemainderX(BigDecimal divisor) {
        return Sequence.of(super.divideAndRemainder(divisor)).map(BigDecimalX::of).toListX();
    }

    public ListX<BigDecimalX> divideAndRemainderX(BigDecimal divisor, MathContext mc) {
        return Sequence.of(super.divideAndRemainder(divisor, mc)).map(BigDecimalX::of).toListX();
    }

    @Override
    public BigDecimalX pow(int n) {
        return of(super.pow(n));
    }

    @Override
    public BigDecimalX pow(int n, MathContext mc) {
        return of(super.pow(n, mc));
    }

    @Override
    public BigDecimalX abs() {
        return of(super.abs());
    }

    @Override
    public BigDecimalX abs(MathContext mc) {
        return of(super.abs(mc));
    }

    @Override
    public BigDecimalX negate() {
        return of(super.negate());
    }

    @Override
    public BigDecimalX negate(MathContext mc) {
        return of(super.negate(mc));
    }

    @Override
    public BigDecimalX plus() {
        return of(super.plus());
    }

    @Override
    public BigDecimalX plus(MathContext mc) {
        return of(super.plus(mc));
    }

    @Override
    public BigDecimalX round(MathContext mc) {
        return of(super.round(mc));
    }

    @Override
    public BigDecimalX setScale(int newScale, RoundingMode roundingMode) {
        return of(super.setScale(newScale, roundingMode));
    }

    @Override
    public BigDecimalX movePointLeft(int n) {
        return of(super.movePointLeft(n));
    }

    @Override
    public BigDecimalX movePointRight(int n) {
        return of(super.movePointRight(n));
    }

    @Override
    public BigDecimalX scaleByPowerOfTen(int n) {
        return of(super.scaleByPowerOfTen(n));
    }

    @Override
    public BigDecimalX stripTrailingZeros() {
        return of(super.stripTrailingZeros());
    }

    @Override
    public BigDecimalX min(BigDecimal val) {
        return of(super.min(val));
    }

    @Override
    public BigDecimalX max(BigDecimal val) {
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
