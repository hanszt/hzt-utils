package hzt.ranges;

import hzt.collections.ArrayX;
import hzt.statistics.NumberStatistics;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface NumberRange<T extends Number> {

    @NotNull T min();

    @NotNull T min(Predicate<T> predicate);

    @NotNull T max();

    @NotNull T max(Predicate<T> predicate);

    @NotNull Number average();

    @NotNull Number average(Predicate<T> predicate);

    @NotNull Number sum();

    @NotNull Number sum(Predicate<T> predicate);

    @NotNull Number stdDev();

    @NotNull Number stdDev(Predicate<T> predicate);

    @NotNull NumberStatistics stats();

    @NotNull NumberStatistics stats(Predicate<T> predicate);

    @NotNull NumberRange<T> filter(Predicate<T> predicate);

    @NotNull NumberRange<T> onEach(Consumer<? super T> numberSupplier);

    @NotNull ArrayX<T> toArrayX();

    @NotNull T[] toArray();
}
