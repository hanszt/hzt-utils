package org.hzt.fx.utils;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.hzt.fx.utils.Listeners.forNewValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ListenersTest {

    @Test
    void testListenerForNewValue() {
        IntegerProperty value = new SimpleIntegerProperty(2);

        value.addListener(forNewValue(newValue -> assertEquals(4, newValue)));

        value.set(4);
    }

    @Test
    void testBindedProperty() {
        IntegerProperty value1 = new SimpleIntegerProperty();
        IntegerProperty value2 = new SimpleIntegerProperty();

        value1.bindBidirectional(value2);

        for (int value : Collections.unmodifiableSet(new HashSet<>(Arrays.asList(1, 2, 4, 56, 400, -4, 6, 3)))) {
            value1.set(value);
            assertEquals(value1.get(), value2.get());
        }

    }

}
