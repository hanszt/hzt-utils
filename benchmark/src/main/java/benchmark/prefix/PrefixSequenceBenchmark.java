package benchmark.prefix;

import hzt.collections.ListX;
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
public class PrefixSequenceBenchmark {
    @Param({"100000"})
    private int nrOfIterations;

    private final ListX<String> list = ListX.build(strings -> {
                for (int i = 0; i < 100_000; i++) {
                    strings.add(String.valueOf(i));
                }
            }
    );

    public PrefixSequenceBenchmark() {
        super();
    }

    @Benchmark
    public ListX<Integer> mapFilterToList() {
        return list.asSequence()
                .map(String::length)
                .filter(i -> i % 2 == 0)
                .toListX();
    }

    public static void main(String[] args) {
        Options options = new OptionsBuilder()
                .include(PrefixSequenceBenchmark.class.getSimpleName())
                .build();
        try {
            new Runner(options).run();
        } catch (RunnerException e) {
            throw new IllegalStateException(e);
        }
    }
}
