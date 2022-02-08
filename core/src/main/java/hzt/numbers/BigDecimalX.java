package hzt.numbers;

import hzt.collections.ArrayX;
import hzt.sequences.Sequence;
import hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@SuppressWarnings("unused")
public final class BigDecimalX extends BigDecimal implements NumberX<BigDecimal>, Transformable<BigDecimalX> {

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
    public @NotNull BigDecimalX add(BigDecimal augend, MathContext mc) {
        return of(super.add(augend, mc));
    }

    @Override
    public @NotNull BigDecimalX subtract(BigDecimal subtrahend) {
        return of(super.subtract(subtrahend));
    }

    @Override
    public @NotNull BigDecimalX subtract(BigDecimal subtrahend, MathContext mc) {
        return of(super.subtract(subtrahend, mc));
    }

    @Override
    public @NotNull BigDecimalX multiply(BigDecimal multiplicand) {
        return of(super.multiply(multiplicand));
    }

    @Override
    public @NotNull BigDecimalX multiply(BigDecimal multiplicand, MathContext mc) {
        return of(super.multiply(multiplicand, mc));
    }

    @Override
    public @NotNull BigDecimalX divide(BigDecimal divisor, int scale, RoundingMode roundingMode) {
        return of(super.divide(divisor, scale, roundingMode));
    }

    @Override
    public @NotNull BigDecimalX divide(BigDecimal divisor, RoundingMode roundingMode) {
        return of(super.divide(divisor, roundingMode));
    }

    @Override
    public @NotNull BigDecimalX divide(BigDecimal divisor, MathContext mc) {
        return of(super.divide(divisor, mc));
    }

    @Override
    public @NotNull BigDecimalX divideToIntegralValue(BigDecimal divisor) {
        return of(super.divideToIntegralValue(divisor));
    }

    @Override
    public @NotNull BigDecimalX divideToIntegralValue(BigDecimal divisor, MathContext mc) {
        return of(super.divideToIntegralValue(divisor, mc));
    }

    @Override
    public @NotNull BigDecimalX remainder(BigDecimal divisor) {
        return of(super.remainder(divisor));
    }

    @Override
    public @NotNull BigDecimalX remainder(BigDecimal divisor, MathContext mc) {
        return of(super.remainder(divisor, mc));
    }

    public ArrayX<BigDecimalX> divideAndRemainderX(BigDecimal divisor) {
        return Sequence.of(super.divideAndRemainder(divisor)).map(BigDecimalX::of).toArrayX(BigDecimalX[]::new);
    }

    public ArrayX<BigDecimalX> divideAndRemainderX(BigDecimal divisor, MathContext mc) {
        return Sequence.of(super.divideAndRemainder(divisor, mc)).map(BigDecimalX::of).toArrayX(BigDecimalX[]::new);
    }

    @Override
    public BigDecimalX pow(int n) {
        return of(super.pow(n));
    }

    @Override
    public @NotNull BigDecimalX pow(int n, MathContext mc) {
        return of(super.pow(n, mc));
    }

    @Override
    public @NotNull BigDecimalX abs() {
        return of(super.abs());
    }

    @Override
    public @NotNull BigDecimalX abs(MathContext mc) {
        return of(super.abs(mc));
    }

    @Override
    public @NotNull BigDecimalX negate() {
        return of(super.negate());
    }

    @Override
    public @NotNull BigDecimalX negate(MathContext mc) {
        return of(super.negate(mc));
    }

    @Override
    public @NotNull BigDecimalX plus() {
        return of(super.plus());
    }

    @Override
    public @NotNull BigDecimalX plus(MathContext mc) {
        return of(super.plus(mc));
    }

    @Override
    public @NotNull BigDecimalX round(MathContext mc) {
        return of(super.round(mc));
    }

    @Override
    public @NotNull BigDecimalX setScale(int newScale, RoundingMode roundingMode) {
        return of(super.setScale(newScale, roundingMode));
    }

    @Override
    public @NotNull BigDecimalX movePointLeft(int n) {
        return of(super.movePointLeft(n));
    }

    @Override
    public @NotNull BigDecimalX movePointRight(int n) {
        return of(super.movePointRight(n));
    }

    @Override
    public @NotNull BigDecimalX scaleByPowerOfTen(int n) {
        return of(super.scaleByPowerOfTen(n));
    }

    @Override
    public @NotNull BigDecimalX stripTrailingZeros() {
        return of(super.stripTrailingZeros());
    }

    @Override
    public @NotNull BigDecimalX sqrt(MathContext mc) {
        return of(super.sqrt(mc));
    }

    @Override
    public @NotNull BigDecimalX min(BigDecimal val) {
        return of(super.min(val));
    }

    @Override
    public @NotNull BigDecimalX max(BigDecimal val) {
        return of(super.max(val));
    }

    @Override
    public @NotNull BigDecimalX ulp() {
        return of(super.ulp());
    }

    @Override
    public @NotNull BigDecimalX get() {
        return this;
    }

    @Override
    public @NotNull BigDecimal getValue() {
        return this;
    }
}
