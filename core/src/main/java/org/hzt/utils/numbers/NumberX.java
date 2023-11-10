package org.hzt.utils.numbers;

import org.hzt.utils.strings.StringX;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

@FunctionalInterface
public interface NumberX<T extends Number> {

    T getValue();

    default IntX toIntX() {
        return IntX.of(getValue());
    }

    default int toInt() {
        return getValue().intValue();
    }

    default LongX toLongX() {
        return LongX.of(getValue());
    }

    default long toLong() {
        return getValue().longValue();
    }

    default DoubleX toDoubleX() {
        return DoubleX.of(getValue());
    }

    default double toDouble() {
        return getValue().doubleValue();
    }

    default BigDecimalX toBigDecimalX() {
        return BigDecimalX.of(getValue());
    }

    default BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(getValue().longValue());
    }

    default BigDecimalX toBigDecimalX(int scale, RoundingMode roundingMode) {
        return BigDecimalX.of(getValue()).setScale(scale, roundingMode);
    }

    default BigDecimal toBigDecimal(int scale, RoundingMode roundingMode) {
        return BigDecimal.valueOf(getValue().longValue()).setScale(scale, roundingMode);
    }

    default BigInteger toBigInteger() {
        return BigDecimalX.of(getValue()).toBigInteger();
    }

    default StringX toStringX() {
        return StringX.of(getValue());
    }
}
