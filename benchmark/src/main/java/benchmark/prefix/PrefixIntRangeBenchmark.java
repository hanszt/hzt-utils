package benchmark.prefix;

import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.statistics.IntStatistics;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.IntSummaryStatistics;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
@State(Scope.Benchmark)
public class PrefixIntRangeBenchmark {
    @Param({"100000"})
    private int nrOfIterations;

    public PrefixIntRangeBenchmark() {
        super();
    }

    @Benchmark
    public IntSummaryStatistics parallelStreamMapFilterToSummaryStats() {
        return IntStream.range(0, nrOfIterations)
                .parallel()
                .map(i -> i * 2)
                .filter(i -> i % 4 == 0)
                .summaryStatistics();
    }

    @Benchmark
    public IntStatistics mapFilterToStats() {
        return IntRange.of(0, nrOfIterations)
                .map(i -> i * 2)
                .filter(i -> i % 4 == 0)
                .stats();
    }

    @Benchmark
    public IntSummaryStatistics streamMapFilterToSummaryStats() {
        return IntStream.range(0, nrOfIterations)
                .map(i -> i * 2)
                .filter(i -> i % 4 == 0)
                .summaryStatistics();
    }

    public static void main(String[] args) {
        Options options = new OptionsBuilder()
                .include(PrefixIntRangeBenchmark.class.getSimpleName())
                .build();
        try {
            new Runner(options).run();
        } catch (RunnerException e) {
            throw new IllegalStateException(e);
        }
    }
}
