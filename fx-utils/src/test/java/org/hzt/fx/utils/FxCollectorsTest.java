package org.hzt.fx.utils;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableFloatArray;
import javafx.collections.ObservableIntegerArray;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static org.hzt.fx.utils.FxCollectors.toObservableFloatArray;
import static org.hzt.fx.utils.FxCollectors.toObservableIntArray;
import static org.hzt.fx.utils.FxCollectors.toObservableList;
import static org.hzt.fx.utils.FxCollectors.toObservableMap;
import static org.hzt.fx.utils.FxCollectors.toObservableSet;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FxCollectorsTest {

    @Test
    void testCollectToObservableList() {
        final AtomicBoolean changeListenerFired = new AtomicBoolean(false);

        final ObservableList<String> strings = IntStream.of(1, 2, 3, 4, 5, 6, 7, 4, 5, 3)
                .sorted()
                .filter(i -> i % 2 == 0)
                .mapToObj(Integer::toString)
                .collect(toObservableList());

        strings.addListener((ListChangeListener<String>) c -> changeListenerFired.set(true));
        final IntegerProperty size = new SimpleIntegerProperty();
        strings.add("Kaas");
        size.bind(Bindings.size(strings));

        assertAll(
                () -> assertTrue(changeListenerFired.get()),
                () -> assertEquals(Arrays.asList("2", "4", "4", "6", "Kaas"), strings),
                () -> assertEquals(5, size.get())
        );
    }

    @Test
    void testCollectToObservableSet() {
        final AtomicBoolean changeListenerFired = new AtomicBoolean(false);

        final ObservableSet<String> strings = IntStream.of(1, 2, 3, 4, 5, 6, 7, 4, 5, 3)
                .sorted()
                .filter(i -> i % 2 == 0)
                .mapToObj(Integer::toString)
                .collect(toObservableSet());

        strings.addListener((SetChangeListener<String>) c -> changeListenerFired.set(true));
        final IntegerProperty size = new SimpleIntegerProperty();
        strings.add("Kaas");
        size.bind(Bindings.size(strings));

        assertAll(
                () -> assertTrue(changeListenerFired.get()),
                () -> assertEquals(new HashSet<>(Arrays.asList("2", "4", "6", "Kaas")), strings),
                () -> assertEquals(4, size.get())
        );
    }

    @Test
    void testCollectToObservableMap() {
        final AtomicBoolean changeListenerFired = new AtomicBoolean(false);

        final ObservableMap<Integer, String> map = IntStream.of(1, 2, 3, 4, 5, 6, 7, 4, 5, 3)
                .sorted()
                .filter(i -> i % 2 == 0)
                .distinct()
                .boxed()
                .collect(toObservableMap(t -> t, t -> Integer.toString(t)));

        map.addListener((MapChangeListener<Integer, String>) c -> changeListenerFired.set(true));
        final IntegerProperty size = new SimpleIntegerProperty();
        map.put(5, "Kaas");
        size.bind(Bindings.size(map));

        final MapX<Integer, String> expectedContent = MapX.of(2, "2", 4, "4", 6, "6", 5, "Kaas");

        assertAll(
                () -> assertTrue(changeListenerFired.get()),
                () -> assertTrue(expectedContent.containsAll(map.entrySet())),
                () -> assertTrue(map.entrySet().containsAll(map.entrySet())),
                () -> assertEquals(4, size.get())
        );
    }

    @Test
    void testCollectToObservableFloatArray() {
        final AtomicBoolean changeListenerFired = new AtomicBoolean(false);

        final ObservableFloatArray floatArray = DoubleSequence.of(1, 2, 3, 4, 5, 6, 7, 4, 5, 3)
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
        final AtomicBoolean changeListenerFired = new AtomicBoolean(false);

        final ObservableIntegerArray intArray = IntSequence.of(1, 2, 3, 4, 5, 6, 7, 4, 5, 3)
                .sorted()
                .filter(i -> i % 2 == 0)
                .collect(toObservableIntArray());

        intArray.addListener((observableArray, sizeChanged, from, to) -> changeListenerFired.set(true));
        intArray.set(0, 3);

        final int[] expected = new int[]{3, 4, 4, 6};

        assertAll(
                () -> assertTrue(changeListenerFired.get()),
                () -> assertArrayEquals(expected, intArray.toArray(new int[expected.length]))
        );
    }

    @Test
    void testStreamCollectToObservableIntArray() {
        final AtomicBoolean changeListenerFired = new AtomicBoolean(false);

        final ObservableIntegerArray intArray = FXCollections
                .observableIntegerArray(IntStream.of(1, 2, 3, 4, 5, 6, 7, 4, 5, 3)
                        .sorted()
                        .filter(i -> i % 2 == 0)
                        .toArray());

        intArray.addListener((observableArray, sizeChanged, from, to) -> changeListenerFired.set(true));
        intArray.set(0, 3);

        final int[] expected = new int[]{3, 4, 4, 6};

        assertAll(
                () -> assertTrue(changeListenerFired.get()),
                () -> assertArrayEquals(expected, intArray.toArray(new int[expected.length]))
        );
    }

    @Test
    void testCollectBoxedTypeToObservableFloatArray() {
        final AtomicBoolean changeListenerFired = new AtomicBoolean(false);

        final ObservableFloatArray floatArray = DoubleStream.of(1, 2, 3, 4, 5, 6, 7, 4, 5, 3)
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
