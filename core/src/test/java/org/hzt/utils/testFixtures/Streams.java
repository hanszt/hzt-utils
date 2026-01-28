package org.hzt.utils.testFixtures;

import java.math.BigInteger;
import java.util.stream.Stream;

public final class Streams {

    private Streams() {
    }

    public static Stream<BigInteger> fibonacci() {
        final var seedValue = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};
        return Stream
                .iterate(seedValue, pair -> new BigInteger[]{pair[1], pair[0].add(pair[1])})
                .map(pair -> pair[0]);
    }
}
