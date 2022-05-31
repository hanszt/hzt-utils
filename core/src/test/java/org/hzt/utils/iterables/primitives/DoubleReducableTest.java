package org.hzt.utils.iterables.primitives;

import org.hzt.utils.numbers.DoubleX;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleReducableTest {

    @Test
    void testReduceDouble() {
        Locale defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);

        final double sum = DoubleSequence.generate(0, d -> d + .1)
                .map(Math::sin)
                .take(1_000_000)
                .reduce(0.0, Double::sum);

        assertEquals("19.96", DoubleX.toRoundedString(sum));

        Locale.setDefault(defaultLocale);
    }

}
