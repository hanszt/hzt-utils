package org.hzt.fx.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableFloatArray;
import javafx.collections.ObservableIntegerArray;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import org.hzt.fx.utils.function.DoubleToFloatFunction;
import org.hzt.fx.utils.function.LongToFloatFunction;
import org.hzt.fx.utils.function.ToFloatFunction;
import org.hzt.utils.collections.primitives.IntMutableList;
import org.hzt.utils.collectors.primitves.DoubleCollector;
import org.hzt.utils.collectors.primitves.IntCollector;
import org.hzt.utils.collectors.primitves.IntCollectors;
import org.hzt.utils.collectors.primitves.LongCollector;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class FxCollectors {

    private FxCollectors() {
    }

    public static <T> Collector<T, ObservableList<T>, ObservableList<T>> toObservableList() {
        return Collector.of(FXCollections::observableArrayList, List::add, (left, right) -> {
            left.addAll(right);
            return left;
        }, Collector.Characteristics.IDENTITY_FINISH);
    }

    public static <T> Collector<T, ?, ObservableSet<T>> toObservableSet() {
        return Collectors.collectingAndThen(Collectors.toSet(), FXCollections::observableSet);
    }

    public static <T, K, V> Collector<T, ?, ObservableMap<K, V>> toObservableMap(
            final Function<? super T, K> keyMapper,
            final Function<? super T, V> valueMapper) {
        return Collectors.collectingAndThen(Collectors.toMap(keyMapper, valueMapper), FXCollections::observableMap);
    }

    public static IntCollector<IntMutableList, ObservableIntegerArray> toObservableIntArray() {
        return IntCollectors.collectingAndThen(IntCollectors.toList(), l -> FXCollections.observableIntegerArray(l.toArray()));
    }

    public static <T> Collector<T, FloatMutableList, ObservableFloatArray> toObservableFloatArray(
            ToFloatFunction<? super T> toFloatFunction) {
        return Collector.of(FloatMutableList::new,
                (floatMutableList, value) -> floatMutableList.add(toFloatFunction.applyAsFloat(value)),
                FloatMutableList::plus,
                floatMutableList -> FXCollections.observableFloatArray(floatMutableList.toArray()));
    }

    public static IntCollector<FloatMutableList, ObservableFloatArray> toObservableFloatArray(IntToFloatFunction mapper) {
        return IntCollector.of(FloatMutableList::new,
                (floatMutableList, value) -> floatMutableList.add(mapper.applyAsFloat(value)),
                floatMutableList -> FXCollections.observableFloatArray(floatMutableList.toArray()));
    }

    public static LongCollector<FloatMutableList, ObservableFloatArray> toObservableFloatArray(LongToFloatFunction mapper) {
        return LongCollector.of(FloatMutableList::new,
                (floatMutableList, value) -> floatMutableList.add(mapper.applyAsFloat(value)),
                floatMutableList -> FXCollections.observableFloatArray(floatMutableList.toArray()));
    }
    public static DoubleCollector<FloatMutableList, ObservableFloatArray> toObservableFloatArray(DoubleToFloatFunction mapper) {
        return DoubleCollector.of(FloatMutableList::new,
                (floatMutableList, value) -> floatMutableList.add(mapper.applyAsFloat(value)),
                floatMutableList -> FXCollections.observableFloatArray(floatMutableList.toArray()));
    }

}
