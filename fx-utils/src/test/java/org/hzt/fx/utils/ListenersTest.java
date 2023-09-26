package org.hzt.fx.utils;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hzt.fx.utils.Listeners.forNewValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ListenersTest {

    @Test
    void testListenerForNewValue() {
        final IntegerProperty value = new SimpleIntegerProperty(2);

        value.addListener(forNewValue(newValue -> assertEquals(4, newValue)));

        value.set(4);
    }

    @Test
    void testBoundProperty() {
        final IntegerProperty value1 = new SimpleIntegerProperty();
        final IntegerProperty value2 = new SimpleIntegerProperty();

        value1.bindBidirectional(value2);

        for (final int value : Set.of(1, 2, 4, 56, 400, -4, 6, 3)) {
            value1.set(value);
            assertEquals(value1.get(), value2.get());
        }

    }

}
