## Benchmark
A module to benchmark hzt utils


## Result 2022-1-28
````
# Run progress: 90,00% complete, ETA 00:01:41
# Fork: 5 of 5
# Warmup Iteration   1: 454,246 ops/s
# Warmup Iteration   2: 466,616 ops/s
# Warmup Iteration   3: 459,251 ops/s
# Warmup Iteration   4: 460,439 ops/s
# Warmup Iteration   5: 437,780 ops/s
Iteration   1: 458,328 ops/s
Iteration   2: 464,409 ops/s
Iteration   3: 454,738 ops/s
Iteration   4: 449,976 ops/s
Iteration   5: 448,160 ops/s


Result "benchmark.prefix.PrefixIterableXBenchmark.mapToList":
447,048 ±(99.9%) 12,494 ops/s [Average]
(min, avg, max) = (379,037, 447,048, 464,409), stdev = 16,679
CI (99.9%): [434,554, 459,542] (assumes normal distribution)


# Run complete. Total time: 00:16:50

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

Benchmark                           (nrOfIterations)   Mode  Cnt     Score     Error  Units
PrefixIterableXBenchmark.anyMatch             100000  thrpt   25  1488,453 ± 124,301  ops/s
PrefixIterableXBenchmark.mapToList            100000  thrpt   25   447,048 ±  12,494  ops/s
````
