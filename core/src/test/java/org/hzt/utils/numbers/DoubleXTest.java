package org.hzt.utils.numbers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoubleXTest {

    @Test
    void testToRoundedString() {
        final Locale locale = Locale.getDefault();

        Locale.setDefault(Locale.US);
        final String roundedString = DoubleX.toRoundedString(Math.PI, 5);

        assertEquals("3.14159", roundedString);

        Locale.setDefault(locale);
    }

    @ParameterizedTest
    @ValueSource(strings = {"8e3", "2.73", "10.34E4", "3", "3.0d", "4.8F", "NaN", "Infinity"})
    void testIsParsableDouble(final String string) {
        assertTrue(DoubleX.isParsableDouble(string));
    }

    @ParameterizedTest
    @ValueSource(strings = {"8e3.3", "hallo", "3,123", "3.0e", "4.8q"})
    void testIsNotAParsableDouble(final String string) {
        assertFalse(DoubleX.isParsableDouble(string));
    }


}
