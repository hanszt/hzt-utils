package hzt.ranges;

import hzt.numbers.IntX;
import hzt.sequences.Sequence;

import java.util.Optional;
import java.util.Random;

public interface IntRange extends Sequence<Integer> {

    static IntRange empty() {
        return IntRange.of(Sequence.empty());
    }

    static IntRange of(Iterable<Integer> integers) {
        return integers::iterator;
    }

    static IntRange of(int start, int end) {
        return start < end ? IntRange.of(Sequence.generate(start, i -> ++i).take(end - start)) :
                IntRange.of(Sequence.generate(start, i -> --i).take(start - end));
    }

    static IntX from(int start) {
        return IntX.of(start);
    }

    static IntRange until(int end) {
        return of(0, end);
    }

    static IntRange closed(int endInclusive) {
        return closed(0, endInclusive);
    }

    static IntRange closed(int start, int endInclusive) {
        return IntRange.of(Sequence.generate(start, i -> i + (start < endInclusive ? 1 : -1))
                .take(Math.abs(endInclusive - start) + 1));
    }

    default IntRange step(int step) {
        return IntRange.of(filter(i -> i % step == 0));
    }

    default int random() {
        return toListX().random();
    }

    default Optional<Integer> findRandom() {
        return toListX().findRandom();
    }

    default int random(Random random) {
        return toListX().random(random);
    }

    default Optional<Integer> findRandom(Random random) {
        return toListX().findRandom(random);
    }
}
