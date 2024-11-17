package org.hzt.utils;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class TimerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerTest.class);

    @Test
    void testTime() {
        final var desiredSize = 1_000;

        final var timer = Timer.measureTimedValue(() ->
                Stream.iterate(new BigInteger[]{BigInteger.ONE, BigInteger.ONE}, p -> new BigInteger[]{p[1], p[0].add(p[1])})
                        .map(p -> p[0])
                        .dropWhile(nr -> nr.toString().length() < desiredSize)
                        .findFirst()
                        .orElseThrow()
        );
        LOGGER.debug("{}", timer);

        final var result = timer.result();

        assertThat(result).isGreaterThan(BigInteger.valueOf(Long.MAX_VALUE));
        assertThat(result.toString().length()).isEqualTo(desiredSize);
    }

}