package org.hzt.fx.utils;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.SetChangeListener;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static org.hzt.fx.utils.FxCollectors.*;
import static org.junit.jupiter.api.Assertions.*;

class FxCollectorsTest {

    @Test
    void testCollectToObservableList() {
        final var changeListenerFired = new AtomicBoolean(false);

        final var strings = IntStream.of(1, 2, 3, 4, 5, 6, 7, 4, 5, 3)
                .sorted()
                .filter(i -> i % 2 == 0)
                .mapToObj(Integer::toString)
                .collect(toObservableList());

        strings.addListener((ListChangeListener<String>) c -> changeListenerFired.set(true));
        IntegerProperty size = new SimpleIntegerProperty();
        strings.add("Kaas");
        size.bind(Bindings.size(strings));

        assertAll(
                () -> assertTrue(changeListenerFired.get()),
                () -> assertEquals(List.of("2", "4", "4", "6", "Kaas"), strings),
                () -> assertEquals(5, size.get())
        );
    }

    @Test
    void testCollectToObservableSet() {
        final var changeListenerFired = new AtomicBoolean(false);

        final var strings = IntStream.of(1, 2, 3, 4, 5, 6, 7, 4, 5, 3)
                .sorted()
                .filter(i -> i % 2 == 0)
                .mapToObj(Integer::toString)
                .collect(toObservableSet());

        strings.addListener((SetChangeListener<String>) c -> changeListenerFired.set(true));
        IntegerProperty size = new SimpleIntegerProperty();
        strings.add("Kaas");
        size.bind(Bindings.size(strings));

        assertAll(
                () -> assertTrue(changeListenerFired.get()),
                () -> assertEquals(Set.of("2", "4", "6", "Kaas"), strings),
                () -> assertEquals(4, size.get())
        );
    }

    @Test
    void testCollectToObservableMap() {
        final var changeListenerFired = new AtomicBoolean(false);

        final var map = IntStream.of(1, 2, 3, 4, 5, 6, 7, 4, 5, 3)
                .sorted()
                .filter(i -> i % 2 == 0)
                .distinct()
                .boxed()
                .collect(toObservableMap(t -> t, t -> Integer.toString(t)));

        map.addListener((MapChangeListener<Integer, String>) c -> changeListenerFired.set(true));
        IntegerProperty size = new SimpleIntegerProperty();
        map.put(5, "Kaas");
        size.bind(Bindings.size(map));

        assertAll(
                () -> assertTrue(changeListenerFired.get()),
                () -> assertEquals(Map.of(2, "2", 4, "4", 6, "6", 5, "Kaas"), map),
                () -> assertEquals(4, size.get())
        );
    }

    @Test
    void testCollectToObservableFloatArray() {
        final var changeListenerFired = new AtomicBoolean(false);

        final var floatArray = DoubleSequence.of(1, 2, 3, 4, 5, 6, 7, 4, 5, 3)
                .sorted()
                .filter(i -> i % 2 == 0)
                .distinct()
                .collect(toObservableFloatArray((double d) -> (float) d));

        floatArray.addListener((observableArray, sizeChanged, from, to) -> changeListenerFired.set(true));
        floatArray.set(0, 3);

        assertAll(
                () -> assertTrue(changeListenerFired.get()),
                () -> assertArrayEquals(new float[] {3.0F, 4.0F, 6.0F}, floatArray.toArray(new float[3]))
        );
    }

    @Test
    void testCollectToObservableIntArray() {
        final var changeListenerFired = new AtomicBoolean(false);

        final var intArray = IntSequence.of(1, 2, 3, 4, 5, 6, 7, 4, 5, 3)
                .sorted()
                .filter(i -> i % 2 == 0)
                .collect(toObservableIntArray());

        intArray.addListener((observableArray, sizeChanged, from, to) -> changeListenerFired.set(true));
        intArray.set(0, 3);

        final var expected = new int[]{3, 4, 4, 6};

        assertAll(
                () -> assertTrue(changeListenerFired.get()),
                () -> assertArrayEquals(expected, intArray.toArray(new int[expected.length]))
        );
    }

    @Test
    void testStreamCollectToObservableIntArray() {
        final var changeListenerFired = new AtomicBoolean(false);

        final var intArray = FXCollections
                .observableIntegerArray(IntStream.of(1, 2, 3, 4, 5, 6, 7, 4, 5, 3)
                        .sorted()
                        .filter(i -> i % 2 == 0)
                        .toArray());

        intArray.addListener((observableArray, sizeChanged, from, to) -> changeListenerFired.set(true));
        intArray.set(0, 3);

        final var expected = new int[]{3, 4, 4, 6};

        assertAll(
                () -> assertTrue(changeListenerFired.get()),
                () -> assertArrayEquals(expected, intArray.toArray(new int[expected.length]))
        );
    }

    @Test
    void testCollectBoxedTypeToObservableFloatArray() {
        final var changeListenerFired = new AtomicBoolean(false);

        final var floatArray = DoubleStream.of(1, 2, 3, 4, 5, 6, 7, 4, 5, 3)
                .sorted()
                .filter(i -> i % 2 == 0)
                .distinct()
                .boxed()
                .collect(toObservableFloatArray(Double::floatValue));

        floatArray.addListener((observableArray, sizeChanged, from, to) -> changeListenerFired.set(true));
        floatArray.set(0, 3);

        assertAll(
                () -> assertTrue(changeListenerFired.get()),
                () -> assertArrayEquals(new float[]{3.0F, 4.0F, 6.0F}, floatArray.toArray(new float[3]))
        );
    }

}
