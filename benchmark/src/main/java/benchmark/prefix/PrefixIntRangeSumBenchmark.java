package benchmark.prefix;

import org.hzt.utils.ranges.IntRange;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.stream.IntStream;

@SuppressWarnings("unused")
@State(Scope.Benchmark)
public class PrefixIntRangeSumBenchmark {

    private static final int UPPER_BOUND_RANGE = 100_000;

    @Param({"100000"})
    private int nrOfIterations;

    public PrefixIntRangeSumBenchmark() {
        super();
    }

    @Benchmark
    public long intSequenceMapFilterSum() {
        return IntRange.of(0, UPPER_BOUND_RANGE)
                .map(i -> i * 2)
                .filter(i -> i % 4 == 0)
                .sum();
    }

    @Benchmark
    public long intListXMapFilterSum() {
        return IntRange.of(0, UPPER_BOUND_RANGE)
                .toListX()
                .map(i -> i * 2)
                .filter(i -> i % 4 == 0)
                .sum();
    }

    @Benchmark
    public long streamMapFilterSum() {
        return IntStream.range(0, UPPER_BOUND_RANGE)
                .map(i -> i * 2)
                .filter(i -> i % 4 == 0)
                .summaryStatistics()
                .getSum();
    }

    @Benchmark
    public long parallelStreamMapFilterSum() {
        return IntStream.range(0, UPPER_BOUND_RANGE)
                .parallel()
                .map(i -> i * 2)
                .filter(i -> i % 4 == 0)
                .summaryStatistics()
                .getSum();
    }

    @Benchmark
    public long loopMapFilterSum() {
        long sum = 0;
        for (int i = 0; i < UPPER_BOUND_RANGE; i++) {
            int i2 = i * 2;
            if (i2 % 4 == 0) {
                sum += i2;
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        Options options = new OptionsBuilder()
                .include(PrefixIntRangeSumBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(2)
                .measurementIterations(3)
                .shouldFailOnError(true)
                .build();
        try {
            new Runner(options).run();
        } catch (RunnerException e) {
            throw new IllegalStateException(e);
        }
    }
}
