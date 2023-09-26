package benchmark.prefix;

import org.hzt.utils.It;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.statistics.DoubleStatistics;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.stream.IntStream;

@SuppressWarnings("unused")
@State(Scope.Benchmark)
public class PrefixDoubleSequenceToStatsBenchmark {

    private static final int SIX = 6;
    private static final int TWO = 2;
    private static final int UPPER_BOUND_RANGE = 100_000;
    @Param({"100000"})
    private int nrOfIterations;

    public PrefixDoubleSequenceToStatsBenchmark() {
        super();
    }

    @Benchmark
    public DoubleStatistics sequenceMapFilterToStats() {
        return IntRange.of(0, UPPER_BOUND_RANGE)
                .map(i -> i * TWO)
                .filter(i -> i % SIX == 0)
                .mapToDouble(It::asDouble)
                .stats();
    }

    @Benchmark
    public DoubleStatistics streamMapFilterToStats() {
        return IntStream.range(0, UPPER_BOUND_RANGE)
                .map(i -> i * TWO)
                .filter(i -> i % SIX == 0)
                .mapToDouble(It::asDouble)
                .collect(DoubleStatistics::new, DoubleStatistics::accept, DoubleStatistics::combine);
    }

    @Benchmark
    public DoubleStatistics parallelStreamMapFilterToStats() {
        return IntStream.range(0, UPPER_BOUND_RANGE)
                .parallel()
                .map(i -> i * TWO)
                .filter(i -> i % SIX == 0)
                .mapToDouble(It::asDouble)
                .collect(DoubleStatistics::new, DoubleStatistics::accept, DoubleStatistics::combine);
    }

    @Benchmark
    public DoubleStatistics loopMapFilterToStats() {
        final var statistics = new DoubleStatistics();
        for (var i = 0; i < UPPER_BOUND_RANGE; i++) {
            final var i2 = i * TWO;
            if (i2 % SIX == 0) {
                statistics.accept(i2);
            }
        }
        return statistics;
    }

    public static void main(final String[] args) {
        final var options = new OptionsBuilder()
                .include(PrefixDoubleSequenceToStatsBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(1)
                .measurementIterations(3)
                .shouldFailOnError(true)
                .build();
        try {
            new Runner(options).run();
        } catch (final RunnerException e) {
            throw new IllegalStateException(e);
        }
    }
}
