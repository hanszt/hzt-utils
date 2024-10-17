package benchmark.prefix;

import org.hzt.utils.gatherers.primitives.IntGatherers;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.stream.Gatherers;
import java.util.stream.IntStream;

public class GathererVsIntGathererBenchmark {

    private static final int[] input = IntStream.range(0, 100_000).toArray();
    public static final int WINDOW_SIZE = 16;

    @Benchmark
    public void intSequenceNormalGathererWindowSliding(final Blackhole blackhole) {
        IntSequence.of(input)
                .gather(Gatherers.windowSliding(WINDOW_SIZE))
                .forEach(blackhole::consume);
    }

    @Benchmark
    public void intSequenceIntGathererWindowSliding(final Blackhole blackhole) {
        IntSequence.of(input)
                .gather(IntGatherers.windowSliding(WINDOW_SIZE))
                .forEach(blackhole::consume);
    }

    @Benchmark
    public void boxedSequenceIntGathererWindowSliding(final Blackhole blackhole) {
        IntSequence.of(input)
                .boxed()
                .gather(IntGatherers.windowSliding(WINDOW_SIZE))
                .forEach(blackhole::consume);
    }

    public static void main(final String... args) {
        final var options = new OptionsBuilder()
                .include(GathererVsIntGathererBenchmark.class.getSimpleName())
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
