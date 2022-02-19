package benchmark.prefix;

import hzt.sequences.primitives.IntSequence;
import hzt.statistics.IntStatistics;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@SuppressWarnings("unused")
@State(Scope.Benchmark)
public class PrefixIntRangeBenchmark {
    @Param({"100000"})
    private int nrOfIterations;

    public PrefixIntRangeBenchmark() {
        super();
    }

    @Benchmark
    public IntStatistics mapFilterToList() {
        return IntSequence.of(0, nrOfIterations)
                .map(i -> i * 2)
                .filter(i -> i % 2 == 0)
                .stats();
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
