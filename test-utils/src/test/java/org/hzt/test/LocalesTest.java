package org.hzt.test;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class LocalesTest {

    @Test
    void testWithFixedLocale() {
        final var locale = Locale.of("to_TO_#Latn");
        assertNotEquals(locale, Locale.getDefault());

        Locales.testWithFixedLocale(locale, l -> assertEquals(locale, Locale.getDefault()));

        assertNotEquals(locale, Locale.getDefault());
    }

}