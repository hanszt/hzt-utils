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

}
