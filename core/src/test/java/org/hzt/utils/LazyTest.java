package org.hzt.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LazyTest {

    @Test
    void testLazyInitializationOfRandomDouble() {
        final Lazy<Double> randomValue = Lazy.of(Math::random);
        final Double d1 = randomValue.get();
        final Double d2 = randomValue.get();

        assertEquals(d2, d1);
    }

    @Test
    void testLazyRun() {
        final Transformable<String> randomValue = Lazy.of(Math::random).run(String::valueOf);

        final String s1 = randomValue.get();
        final String s2 = randomValue.get();

        assertEquals(s1, s2);
    }

}
