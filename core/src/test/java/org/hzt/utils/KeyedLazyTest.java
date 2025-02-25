package org.hzt.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyedLazyTest {

    private final KeyedLazy<String, String> lazyString = KeyedLazy.of("lazykey", unused -> "lazyvalue");
    private final KeyedLazy<String, String> eagerString = KeyedLazy.of("eagerkey", "eagervalue");
    private final KeyedLazy<String, Float> lazyFloat = KeyedLazy.of("lazyfloatkey", unused -> 2.4f);

    @Test
    void test() {
        assertFalse(lazyString.isLoaded());
        assertEquals("lazyvalue", this.lazyString.get());
        assertTrue(lazyString.isLoaded());
        assertTrue(eagerString.isLoaded());
        assertEquals("eagervalue", this.eagerString.get());
        assertTrue(eagerString.isLoaded());
        assertEquals(Float.valueOf(2.4f), this.lazyFloat.get());
    }
}