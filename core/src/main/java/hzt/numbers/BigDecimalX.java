package hzt.numbers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public final class BigDecimalX extends Number implements NumberX {

    private final BigDecimal bigDecimal;

    private BigDecimalX(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public static BigDecimalX of(BigDecimal bigDecimal) {
        return new BigDecimalX(bigDecimal);
    }

    public static BigDecimal valueOf(long unscaledVal, int scale) {
        return BigDecimal.valueOf(unscaledVal, scale);
    }

    public static BigDecimal valueOf(long val) {
        return BigDecimal.valueOf(val);
    }

    public static BigDecimal valueOf(double val) {
        return BigDecimal.valueOf(val);
    }

    public BigDecimal add(BigDecimal augend) {
        return bigDecimal.add(augend);
    }

    public BigDecimal add(BigDecimal augend, MathContext mc) {
        return bigDecimal.add(augend, mc);
    }

    public BigDecimal subtract(BigDecimal subtrahend) {
        return bigDecimal.subtract(subtrahend);
    }

    public BigDecimal subtract(BigDecimal subtrahend, MathContext mc) {
        return bigDecimal.subtract(subtrahend, mc);
    }

    public BigDecimal multiply(BigDecimal multiplicand) {
        return bigDecimal.multiply(multiplicand);
    }

    public BigDecimal multiply(BigDecimal multiplicand, MathContext mc) {
        return bigDecimal.multiply(multiplicand, mc);
    }

    public BigDecimal divide(BigDecimal divisor, int scale, RoundingMode roundingMode) {
        return bigDecimal.divide(divisor, scale, roundingMode);
    }

    public BigDecimal divide(BigDecimal divisor, RoundingMode roundingMode) {
        return bigDecimal.divide(divisor, roundingMode);
    }

    public BigDecimal divide(BigDecimal divisor, MathContext mc) {
        return bigDecimal.divide(divisor, mc);
    }

    public BigDecimal divideToIntegralValue(BigDecimal divisor) {
        return bigDecimal.divideToIntegralValue(divisor);
    }

    public BigDecimal divideToIntegralValue(BigDecimal divisor, MathContext mc) {
        return bigDecimal.divideToIntegralValue(divisor, mc);
    }

    public BigDecimal remainder(BigDecimal divisor) {
        return bigDecimal.remainder(divisor);
    }

    public BigDecimal remainder(BigDecimal divisor, MathContext mc) {
        return bigDecimal.remainder(divisor, mc);
    }

    public BigDecimal[] divideAndRemainder(BigDecimal divisor) {
        return bigDecimal.divideAndRemainder(divisor);
    }

    public BigDecimal[] divideAndRemainder(BigDecimal divisor, MathContext mc) {
        return bigDecimal.divideAndRemainder(divisor, mc);
    }

    public BigDecimal pow(int n) {
        return bigDecimal.pow(n);
    }

    public BigDecimal pow(int n, MathContext mc) {
        return bigDecimal.pow(n, mc);
    }

    public BigDecimal abs() {
        return bigDecimal.abs();
    }

    public BigDecimal abs(MathContext mc) {
        return bigDecimal.abs(mc);
    }

    public BigDecimal negate() {
        return bigDecimal.negate();
    }

    public BigDecimal negate(MathContext mc) {
        return bigDecimal.negate(mc);
    }

    public BigDecimal plus() {
        return bigDecimal.plus();
    }

    public BigDecimal plus(MathContext mc) {
        return bigDecimal.plus(mc);
    }

    public int signum() {
        return bigDecimal.signum();
    }

    public int scale() {
        return bigDecimal.scale();
    }

    public int precision() {
        return bigDecimal.precision();
    }

    public BigInteger unscaledValue() {
        return bigDecimal.unscaledValue();
    }

    public BigDecimal round(MathContext mc) {
        return bigDecimal.round(mc);
    }

    public BigDecimal setScale(int newScale, RoundingMode roundingMode) {
        return bigDecimal.setScale(newScale, roundingMode);
    }

    public BigDecimal movePointLeft(int n) {
        return bigDecimal.movePointLeft(n);
    }

    public BigDecimal movePointRight(int n) {
        return bigDecimal.movePointRight(n);
    }

    public BigDecimal scaleByPowerOfTen(int n) {
        return bigDecimal.scaleByPowerOfTen(n);
    }

    public BigDecimal stripTrailingZeros() {
        return bigDecimal.stripTrailingZeros();
    }

    public int compareTo(BigDecimal val) {
        return bigDecimal.compareTo(val);
    }

    public BigDecimal min(BigDecimal val) {
        return bigDecimal.min(val);
    }

    public BigDecimal max(BigDecimal val) {
        return bigDecimal.max(val);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BigDecimalX that = (BigDecimalX) o;
        return bigDecimal.equals(that.bigDecimal);
    }

    @Override
    public int hashCode() {
        return bigDecimal.hashCode();
    }

    @Override
    public String toString() {
        return bigDecimal.toString();
    }

    public String toEngineeringString() {
        return bigDecimal.toEngineeringString();
    }

    public String toPlainString() {
        return bigDecimal.toPlainString();
    }

    public BigInteger toBigInteger() {
        return bigDecimal.toBigInteger();
    }

    public BigInteger toBigIntegerExact() {
        return bigDecimal.toBigIntegerExact();
    }

    @Override
    public long longValue() {
        return bigDecimal.longValue();
    }

    public long longValueExact() {
        return bigDecimal.longValueExact();
    }

    @Override
    public int intValue() {
        return bigDecimal.intValue();
    }

    public int intValueExact() {
        return bigDecimal.intValueExact();
    }

    public short shortValueExact() {
        return bigDecimal.shortValueExact();
    }

    public byte byteValueExact() {
        return bigDecimal.byteValueExact();
    }

    @Override
    public float floatValue() {
        return bigDecimal.floatValue();
    }

    @Override
    public double doubleValue() {
        return bigDecimal.doubleValue();
    }

    public BigDecimal ulp() {
        return bigDecimal.ulp();
    }

    @Override
    public byte byteValue() {
        return bigDecimal.byteValue();
    }

    @Override
    public short shortValue() {
        return bigDecimal.shortValue();
    }
}
