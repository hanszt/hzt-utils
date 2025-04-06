package org.hzt.demo.function;

import org.hzt.utils.function.BiFunction;
import org.hzt.utils.function.Functions;

public final class IntegerFunctions {

    private IntegerFunctions() {
    }

    public static final BiFunction<Integer, Integer, Integer> sum = new BiFunction<Integer, Integer, Integer>() {
        public Integer apply(final Integer i1, final Integer i2) {
            return i1 + i2;
        }
    };

    public static Functions.AbstractFunction<Integer, Integer> plus(final int other) {
        return new Functions.AbstractFunction<Integer, Integer>() {
            public Integer apply(final Integer i) {
                return i + other;
            }
        };
    }

    public static Functions.AbstractFunction<Integer, Integer> times(final int other) {
        return new Functions.AbstractFunction<Integer, Integer>() {
            public Integer apply(final Integer i) {
                return i * other;
            }
        };
    }

    public static Functions.AbstractFunction<Integer, Integer> mod(final int other) {
        return new Functions.AbstractFunction<Integer, Integer>() {
            public Integer apply(final Integer i) {
                return i % other;
            }
        };
    }
}
