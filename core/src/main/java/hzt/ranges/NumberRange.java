package hzt.ranges;

import hzt.collections.ArrayX;
import hzt.statistics.NumberStatistics;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface NumberRange<T extends Number> {

    @NotNull T min();

    @NotNull T max();

    @NotNull Number average();

    @NotNull Number sum();

    @NotNull Number stdDev();

    @NotNull NumberStatistics stats();

    @NotNull NumberRange<T> onEach(Consumer<? super T> numberSupplier);

    @NotNull ArrayX<T> toArrayX();

    @NotNull T[] toArray();
}
