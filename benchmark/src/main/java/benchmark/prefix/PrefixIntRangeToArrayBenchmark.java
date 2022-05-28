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
public class PrefixIntRangeToArrayBenchmark {

    private static final int UPPER_BOUND_RANGE = 100_000;
    @Param({"100000"})
    private int nrOfIterations;

    public PrefixIntRangeToArrayBenchmark() {
        super();
    }

    @Benchmark
    public int[] loopMapFilterToArray() {
        int[] array = new int[UPPER_BOUND_RANGE / 2];
        int index = 0;
        for (int i = 0; i < UPPER_BOUND_RANGE; i++) {
            int i2 = i * 2;
            if (i2 % 4 == 0) {
                array[index] = i2;
                index++;
            }
        }
        return array;
    }

    @Benchmark
    public int[] intRangeMapFilterToArray() {
        return IntRange.of(0, UPPER_BOUND_RANGE)
                .map(i -> i * 2)
                .filter(i -> i % 4 == 0)
                .toArray();
    }

    @Benchmark
    public int[] streamMapFilterToArray() {
        return IntStream.range(0, UPPER_BOUND_RANGE)
                .map(i -> i * 2)
                .filter(i -> i % 4 == 0)
                .toArray();
    }

    @Benchmark
    public int[] parallelStreamMapFilterToArray() {
        return IntStream.range(0, UPPER_BOUND_RANGE)
                .parallel()
                .map(i -> i * 2)
                .filter(i -> i % 4 == 0)
                .toArray();
    }

    public static void main(String[] args) {
        Options options = new OptionsBuilder()
                .include(PrefixIntRangeToArrayBenchmark.class.getSimpleName())
                .forks(2)
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
