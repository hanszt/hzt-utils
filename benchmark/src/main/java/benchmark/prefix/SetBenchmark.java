package benchmark.prefix;

import org.hzt.utils.collections.SetX;
import org.hzt.utils.collections.primitives.IntSet;
import org.hzt.utils.ranges.IntRange;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@State(Scope.Benchmark)
public class SetBenchmark {

    private static final List<Integer> list = IntStream.range(0, 50_000).boxed().toList();
    private static final IntRange range = IntRange.of(0, 100_000);
    private static final IntSet intSet = range.toSet();
    private static final SetX<Integer> setX = range.boxed().toSetX();
    private static final Set<Integer> set = range.boxed().toSet();
    private static final Set<Integer> naiveSet = new AbstractSet<>() {
        @Override
        public Iterator<Integer> iterator() {
            return set.iterator();
        }

        @Override
        public int size() {
            return set.size();
        }
    };

    @Benchmark
    public boolean intSetContains() {
        return intSet.containsAll(list);
    }

    @Benchmark
    public boolean naiveSetContains() {
        return naiveSet.containsAll(list);
    }

    @Benchmark
    public boolean setXContains() {
        return setX.containsAll(list);
    }

    @Benchmark
    public boolean setContains() {
        return set.containsAll(list);
    }

    public static void main(final String... args) {
        final var options = new OptionsBuilder()
                .include(SetBenchmark.class.getSimpleName())
                .addProfiler(GCProfiler.class)
                .forks(2)
                .warmupIterations(2)
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
