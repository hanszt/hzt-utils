package org.hzt.utils.iterables.primitives;

import org.hzt.utils.statistics.NumberStatistics;

interface PrimitiveNumerable<P> {

    long count();

    long count(P predicate);

    NumberStatistics stats();

    NumberStatistics stats(P predicate);
}
