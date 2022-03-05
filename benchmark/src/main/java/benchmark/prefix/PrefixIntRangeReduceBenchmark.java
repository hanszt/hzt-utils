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
public class PrefixIntRangeReduceBenchmark {
    @Param({"100000"})
    private int nrOfIterations;

    public PrefixIntRangeReduceBenchmark() {
        super();
    }

    @Benchmark
    public long mapFilterSum() {
        return IntRange.of(0, nrOfIterations)
                .map(i -> i * 2)
                .filter(i -> i % 4 == 0)
                .sum();
    }

    @Benchmark
    public long intListMapFilterSum() {
        return IntRange.of(0, nrOfIterations)
                .toListX()
                .map(i -> i * 2)
                .filter(i -> i % 4 == 0)
                .sum();
    }

    @Benchmark
    public long streamMapFilterSum() {
        return IntStream.range(0, nrOfIterations)
                .map(i -> i * 2)
                .filter(i -> i % 4 == 0)
                .sum();
    }

    @Benchmark
    public long parallelStreamMapFilterSum() {
        return IntStream.range(0, nrOfIterations)
                .parallel()
                .map(i -> i * 2)
                .filter(i -> i % 4 == 0)
                .sum();
    }

    @Benchmark
    public long loopMapFilterSum() {
        long sum = 0;
        for (int i = 0; i < nrOfIterations; i++) {
            int i2 = i * 2;
            if (i2 % 4 == 0) {
                sum += i2;
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        Options options = new OptionsBuilder()
                .include(PrefixIntRangeReduceBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(2)
                .shouldFailOnError(true)
                .measurementIterations(3)
                .build();
        try {
            new Runner(options).run();
        } catch (RunnerException e) {
            throw new IllegalStateException(e);
        }
    }
}
