package org.hzt.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LazyTest {

    @Test
    void testLazyInitializationOfRandomDouble() {
        final var randomValue = Lazy.of(Math::random);
        final var d1 = randomValue.get();
        final var d2 = randomValue.get();

        assertEquals(d2, d1);
    }

    @Test
    void testLazyRun() {
        final var randomValue = Lazy.of(Math::random).run(String::valueOf);

        final var s1 = randomValue.get();
        final var s2 = randomValue.get();

        assertEquals(s1, s2);
    }

}
