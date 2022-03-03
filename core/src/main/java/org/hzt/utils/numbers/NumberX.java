package org.hzt.utils.numbers;

import org.hzt.utils.strings.StringX;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.math.RoundingMode;

public interface NumberX<T extends Number> {

    @NotNull T getValue();

    default @NotNull IntX toIntX() {
        return IntX.of(getValue());
    }

    default @NotNull LongX toLongX() {
        return LongX.of(getValue());
    }

    default @NotNull DoubleX toDoubleX() {
        return DoubleX.of(getValue());
    }

    default @NotNull BigDecimalX toBigDecimalX() {
        return BigDecimalX.of(getValue());
    }

    default @NotNull BigDecimalX toBigDecimalX(int scale, RoundingMode roundingMode) {
        return BigDecimalX.of(getValue()).setScale(scale, roundingMode);
    }

    default @NotNull BigInteger toBigInteger() {
        return BigDecimalX.of(getValue()).toBigInteger();
    }

    default @NotNull StringX toStringX() {
        return StringX.of(getValue());
    }
}
