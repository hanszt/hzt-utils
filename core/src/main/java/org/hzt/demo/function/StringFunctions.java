package org.hzt.demo.function;

import org.hzt.utils.function.BiFunction;
import org.hzt.utils.function.Function;
import org.hzt.utils.function.Predicate;

public final class StringFunctions {

    private StringFunctions() {
    }

    public static final Function<String, Integer> toStringLength = new Function<String, Integer>() {
        public Integer apply(final String s) {
            return s.length();
        }
    };

    public static final BiFunction<Integer, String, Integer> plusStringLength = new BiFunction<Integer, String, Integer>() {
        public Integer apply(final Integer acc, final String s) {
            return acc + s.length();
        }
    };

    public static Predicate<? super String> startsWith(final String prefix) {
        return new Predicate<String>() {
            public boolean test(final String s) {
                return s.startsWith(prefix);
            }
        };
    }
}
