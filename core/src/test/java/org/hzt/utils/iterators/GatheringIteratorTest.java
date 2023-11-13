package org.hzt.utils.iterators;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.hzt.utils.gatherers.GatherersX.takeWhileIncluding;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GatheringIteratorTest {

    @Test
    void testTakeWhileGatherer() {
        final var counter = new AtomicInteger();

        new GatheringIterator<>(new GeneratorIterator<>(() -> 0, i -> i + 1), takeWhileIncluding(i -> i != 100)).forEachRemaining(s -> counter.incrementAndGet());

        assertEquals(101, counter.get());
    }
}
