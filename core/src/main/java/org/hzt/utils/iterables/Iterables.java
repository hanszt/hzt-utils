package org.hzt.utils.iterables;

import java.util.function.Consumer;

public final class Iterables {

    private Iterables() {
    }

    /**
     * A convenience function for combining two consuming iterables like SpinedBuffer for example
     *
     * @param ci1 consuming iterable 1
     * @param ci2 consuming iterable 2
     * @return consuming iterable 1
     * @param <T> the type of the iterable
     * @param <CI> the Consuming Iterable type
     */
    public static <T, CI extends Consumer<? super T> & Iterable<? extends T>> CI combine(CI ci1, CI ci2) {
        ci2.forEach(ci1);
        return ci1;
    }
}
