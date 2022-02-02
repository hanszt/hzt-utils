package benchmark.prefix;

import hzt.iterables.IterableX;
import hzt.sequences.Sequence;
import hzt.strings.StringX;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

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
        return Sequence.range(0, nrOfIterations).toListOf(StringX::valueOf);
    }
    
    @Benchmark
    public boolean anyMatch() {
        return Sequence.range(0, nrOfIterations).any(x -> x == -1);
    }

}
