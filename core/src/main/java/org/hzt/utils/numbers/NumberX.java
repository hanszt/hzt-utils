package org.hzt.utils.numbers;

import org.hzt.utils.strings.StringX;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

@FunctionalInterface
public interface NumberX<T extends Number> {

    @NotNull T getValue();

    default @NotNull IntX toIntX() {
        return IntX.of(getValue());
    }

    default int toInt() {
        return getValue().intValue();
    }

    default @NotNull LongX toLongX() {
        return LongX.of(getValue());
    }

    default long toLong() {
        return getValue().longValue();
    }

    default @NotNull DoubleX toDoubleX() {
        return DoubleX.of(getValue());
    }

    default double toDouble() {
        return getValue().doubleValue();
    }

    default @NotNull BigDecimalX toBigDecimalX() {
        return BigDecimalX.of(getValue());
    }

    default @NotNull BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(getValue().longValue());
    }

    default @NotNull BigDecimalX toBigDecimalX(int scale, RoundingMode roundingMode) {
        return BigDecimalX.of(getValue()).setScale(scale, roundingMode);
    }

    default @NotNull BigDecimal toBigDecimal(int scale, RoundingMode roundingMode) {
        return BigDecimal.valueOf(getValue().longValue()).setScale(scale, roundingMode);
    }

    default @NotNull BigInteger toBigInteger() {
        return BigDecimalX.of(getValue()).toBigInteger();
    }

    default @NotNull StringX toStringX() {
        return StringX.of(getValue());
    }
}
