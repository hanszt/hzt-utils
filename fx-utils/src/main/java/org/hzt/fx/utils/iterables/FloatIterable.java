package org.hzt.fx.utils.iterables;

import org.hzt.fx.utils.iterators.FloatIterator;

@FunctionalInterface
public interface FloatIterable extends Iterable<Float> {

    @Override
    FloatIterator iterator();
}
