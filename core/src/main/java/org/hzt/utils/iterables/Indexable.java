package org.hzt.utils.iterables;

import org.hzt.utils.sequences.primitives.IntSequence;

import java.util.Spliterators;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@FunctionalInterface
@SuppressWarnings("squid:S1711")
public interface Indexable<T> extends IndexedIterable<T> {

    default IntSequence indices() {
        return IntSequence.of(this::indexIterator);
    }

    default IntStream indicesAsStream() {
        final var indexSpliterator = Spliterators.spliteratorUnknownSize(indexIterator(), 0);
        return StreamSupport.intStream(indexSpliterator, false);
    }
}
