package benchmark.prefix;

import org.hzt.utils.numbers.IntX;
import org.hzt.utils.sequences.Sequence;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
@State(Scope.Benchmark)
public class PrefixSequenceMapFilterToListBenchmark {
    @Param({"100000"})
    private int nrOfIterations;

    private final List<String> list = IntStream.range(0, 100_000)
            .mapToObj(String::valueOf)
            .collect(Collectors.toUnmodifiableList());

    public PrefixSequenceMapFilterToListBenchmark() {
        super();
    }

    @Benchmark
    public List<Integer> sequenceOfListMapFilterToList() {
        return Sequence.of(list)
                .map(String::length)
                .filter(IntX::isEven)
                .toList();
    }

    @Benchmark
    public List<Integer> streamMapFilterToList() {
        return list.stream()
                .map(String::length)
                .filter(IntX::isEven)
                .collect(Collectors.toUnmodifiableList());
    }

    @Benchmark
    public List<Integer> parallelStreamMapFilterToList() {
        return list.parallelStream()
                .map(String::length)
                .filter(IntX::isEven)
                .collect(Collectors.toUnmodifiableList());
    }

    @Benchmark
    public List<Integer> imperativeMapFilterToList() {
        List<Integer> result = new ArrayList<>();
        for (String s : list) {
            int length = s.length();
            if (IntX.isEven(length)) {
                result.add(length);
            }
        }
        return List.copyOf(result);
    }

    public static void main(String[] args) {
        Options options = new OptionsBuilder()
                .include(PrefixSequenceMapFilterToListBenchmark.class.getSimpleName())
                .shouldFailOnError(true)
                .build();
        try {
            new Runner(options).run();
        } catch (RunnerException e) {
            throw new IllegalStateException(e);
        }
    }
}
