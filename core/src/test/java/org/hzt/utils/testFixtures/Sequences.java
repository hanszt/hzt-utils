package org.hzt.utils.testFixtures;

import org.hzt.utils.sequences.Sequence;

import java.math.BigInteger;

public final class Sequences {

    private Sequences() {
    }

    public static Sequence<BigInteger> fibonacci() {
        final var seedValue = new BigInteger[]{BigInteger.ZERO, BigInteger.ONE};
        return Sequence
                .iterate(seedValue, pair -> new BigInteger[]{pair[1], pair[0].add(pair[1])})
                .map(pair -> pair[0]);
    }
}
