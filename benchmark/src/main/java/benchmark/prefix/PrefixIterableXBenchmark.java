package benchmark.prefix;

import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.strings.StringX;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;

@SuppressWarnings("unused")
@State(Scope.Benchmark)
public class PrefixIterableXBenchmark {
    @Param({"100000"})
    private int nrOfIterations;

    public PrefixIterableXBenchmark() {
        super();
    }

    @Benchmark
    public List<StringX> mapToList() {
        return IntSequence.of(0, nrOfIterations).boxed().toListOf(StringX::of);
    }
    
    @Benchmark
    public boolean anyMatch() {
        return IntSequence.of(0, nrOfIterations).any(x -> x == -1);
    }

    public static void main(String[] args) {
        Options options = new OptionsBuilder()
                .include(PrefixIterableXBenchmark.class.getSimpleName())
                .build();
        try {
            new Runner(options).run();
        } catch (RunnerException e) {
            throw new IllegalStateException(e);
        }
    }
}
