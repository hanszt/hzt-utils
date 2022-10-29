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

    public static <T> Collector<T, FloatList, ObservableFloatArray> toObservableFloatArray(ToFloatFunction<? super T> toFloatFunction) {
        return Collector.of(FloatList::new,
                (floatList, value) -> floatList.add(toFloatFunction.applyAsFloat(value)),
                FloatList::plus,
                floatList -> FXCollections.observableFloatArray(floatList.toArray()));
    }

    public static IntCollector<FloatList, ObservableFloatArray> toObservableFloatArray(IntToFloatFunction mapper) {
        return IntCollector.of(FloatList::new,
                (floatList, value) -> floatList.add(mapper.applyAsFloat(value)),
                floatList -> FXCollections.observableFloatArray(floatList.toArray()));
    }

    public static LongCollector<FloatList, ObservableFloatArray> toObservableFloatArray(LongToFloatFunction mapper) {
        return LongCollector.of(FloatList::new,
                (floatList, value) -> floatList.add(mapper.applyAsFloat(value)),
                floatList -> FXCollections.observableFloatArray(floatList.toArray()));
    }
    public static DoubleCollector<FloatList, ObservableFloatArray> toObservableFloatArray(DoubleToFloatFunction mapper) {
        return DoubleCollector.of(FloatList::new,
                (floatList, value) -> floatList.add(mapper.applyAsFloat(value)),
                floatList -> FXCollections.observableFloatArray(floatList.toArray()));
    }

}
