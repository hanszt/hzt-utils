package org.hzt.demo.function;

import org.hzt.utils.function.Functions.AbstractBiFunction;
import org.hzt.utils.function.Functions.AbstractFunction;
import org.hzt.utils.function.Functions.AbstractPredicate;

public final class StringFunctions {

    private StringFunctions() {
    }

    public static final AbstractFunction<String, Integer> toStringLength = new AbstractFunction<String, Integer>() {
        public Integer apply(final String s) {
            return s.length();
        }
    };

    public static final AbstractBiFunction<Integer, String, Integer> plusStringLength = new AbstractBiFunction<Integer, String, Integer>() {
        public Integer apply(final Integer acc, final String s) {
            return acc + s.length();
        }
    };

    public static AbstractPredicate<? super String> startsWith(final String prefix) {
        return new AbstractPredicate<String>() {
            public boolean test(final String s) {
                return s.startsWith(prefix);
            }
        };
    }
}
