package org.hzt.utils.iterables.primitives;

import org.hzt.utils.statistics.NumberStatistics;
import org.jetbrains.annotations.NotNull;

public interface PrimitiveNumerable<P> {

    long count();

    long count(@NotNull P predicate);

    @NotNull NumberStatistics stats();

    @NotNull NumberStatistics stats(@NotNull P predicate);
}
