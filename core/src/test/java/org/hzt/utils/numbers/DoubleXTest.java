package org.hzt.utils.numbers;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class DoubleXTest {

    @Test
    void testToRoundedString() {
        final Locale locale = Locale.getDefault();

        Locale.setDefault(Locale.US);
        final String roundedString = DoubleX.toRoundedString(Math.PI, 5);

        assertEquals("3.14159", roundedString);

        Locale.setDefault(locale);
    }
}
