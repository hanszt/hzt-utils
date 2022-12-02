package org.hzt.fx.utils.sequences;

import javafx.collections.FXCollections;
import org.hzt.fx.utils.collections.FloatArrayList;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FloatSequenceTest {

    @Test
    void testMapToArray() {
        final var floats = FloatSequence.of(1, 2, 3, 4, 5)
                .map(f -> f * .5F)
                .toArray();

        println(Arrays.toString(floats));

        var expected = new float[]{.5F, 1.0F, 1.5F, 2F, 2.5F};

        assertArrayEquals(expected, floats);
    }

    @Test
    void testFilterToList() {
        final var floats = FloatSequence.of(1, 2, 3, 4, 5)
                .filter(i ->i % 2 == 0)
                .toList();

        println(floats);

        final var expected = new FloatArrayList();
        expected.addAll(2.0F, 4.0F);

        assertEquals(expected, floats);
    }

    @Test
    void testFilterToObservableArray() {
        final var floats = FloatSequence.of(1, 2, 3, 4, 5)
                .filter(i ->i % 2 == 0)
                .toObservableArray();

        println(floats);

        final var expected = FXCollections.observableFloatArray(2.0F, 4.0F);

        assertEquals(expected.toString(), floats.toString());
    }

}
