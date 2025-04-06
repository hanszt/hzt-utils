package org.hzt.demo.function;

import org.hzt.utils.function.BiFunction;
import org.hzt.utils.function.Function;

public final class StringFunctions {

    public static final Function<String, Integer> stringLength = new Function<String, Integer>() {
        public Integer apply(final String s) {
            return s.length();
        }
    };

    public static final BiFunction<Integer, String, Integer> plusStringLength = new BiFunction<Integer, String, Integer>() {
        public Integer apply(final Integer acc, final String s) {
            return acc + s.length();
        }
    };

    private StringFunctions() {
    }
}
