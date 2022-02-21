package hzt.iterables.primitives;

import hzt.statistics.NumberStatistics;
import org.jetbrains.annotations.NotNull;

public interface PrimitiveNumerable<P> {

    long count();

    long count(@NotNull P predicate);

    @NotNull NumberStatistics stats();

    @NotNull NumberStatistics stats(@NotNull P predicate);
}
