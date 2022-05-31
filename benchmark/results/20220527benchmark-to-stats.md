````
"C:\Program Files\Java\jdk-17\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=64937:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin" -Dfile.encoding=UTF-8 -classpath C:\_Programming\Java\hzt-utils\benchmark\target\classes;C:\_Programming\Java\hzt-utils\core\target\classes;C:\Users\hzuiderv\.m2\repository\org\jetbrains\annotations\22.0.0\annotations-22.0.0.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-core\1.34\jmh-core-1.34.jar;C:\Users\hzuiderv\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\hzuiderv\.m2\repository\org\apache\commons\commons-math3\3.2\commons-math3-3.2.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.34\jmh-generator-annprocess-1.34.jar benchmark.prefix.PrefixDoubleSequenceToStatsBenchmark
# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=64937:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 1 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixDoubleSequenceToStatsBenchmark.loopMapFilterToStats
# Parameters: (nrOfIterations = 100000)

# Run progress: 0,00% complete, ETA 00:02:40
# Fork: 1 of 1
# Warmup Iteration   1: 3783,616 ops/s
Iteration   1: 4034,793 ops/s
Iteration   2: 4317,018 ops/s
Iteration   3: 5486,583 ops/s


Result "benchmark.prefix.PrefixDoubleSequenceToStatsBenchmark.loopMapFilterToStats":
  4612,798 ±(99.9%) 14043,377 ops/s [Average]
  (min, avg, max) = (4034,793, 4612,798, 5486,583), stdev = 769,765
  CI (99.9%): [≈ 0, 18656,175] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=64937:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 1 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixDoubleSequenceToStatsBenchmark.parallelStreamMapFilterToStats
# Parameters: (nrOfIterations = 100000)

# Run progress: 25,00% complete, ETA 00:02:03
# Fork: 1 of 1
# Warmup Iteration   1: 10021,785 ops/s
Iteration   1: 9961,676 ops/s
Iteration   2: 9639,055 ops/s
Iteration   3: 9627,949 ops/s


Result "benchmark.prefix.PrefixDoubleSequenceToStatsBenchmark.parallelStreamMapFilterToStats":
  9742,893 ±(99.9%) 3458,150 ops/s [Average]
  (min, avg, max) = (9627,949, 9742,893, 9961,676), stdev = 189,553
  CI (99.9%): [6284,743, 13201,044] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=64937:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 1 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixDoubleSequenceToStatsBenchmark.sequenceMapFilterToStats
# Parameters: (nrOfIterations = 100000)

# Run progress: 50,00% complete, ETA 00:01:22
# Fork: 1 of 1
# Warmup Iteration   1: 2585,943 ops/s
Iteration   1: 2532,547 ops/s
Iteration   2: 2544,868 ops/s
Iteration   3: 2561,412 ops/s


Result "benchmark.prefix.PrefixDoubleSequenceToStatsBenchmark.sequenceMapFilterToStats":
  2546,276 ±(99.9%) 264,243 ops/s [Average]
  (min, avg, max) = (2532,547, 2546,276, 2561,412), stdev = 14,484
  CI (99.9%): [2282,033, 2810,519] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=64937:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 1 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixDoubleSequenceToStatsBenchmark.streamMapFilterToStats
# Parameters: (nrOfIterations = 100000)

# Run progress: 75,00% complete, ETA 00:00:41
# Fork: 1 of 1
# Warmup Iteration   1: 4844,932 ops/s
Iteration   1: 4844,104 ops/s
Iteration   2: 4829,381 ops/s
Iteration   3: 4918,879 ops/s


Result "benchmark.prefix.PrefixDoubleSequenceToStatsBenchmark.streamMapFilterToStats":
  4864,121 ±(99.9%) 875,511 ops/s [Average]
  (min, avg, max) = (4829,381, 4864,121, 4918,879), stdev = 47,990
  CI (99.9%): [3988,610, 5739,632] (assumes normal distribution)


# Run complete. Total time: 00:02:44

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

NOTE: Current JVM experimentally supports Compiler Blackholes, and they are in use. Please exercise
extra caution when trusting the results, look into the generated code to check the benchmark still
works, and factor in a small probability of new VM bugs. Additionally, while comparisons between
different JVMs are already problematic, the performance difference caused by different Blackhole
modes can be very significant. Please make sure you use the consistent Blackhole mode for comparisons.

Benchmark                                                            (nrOfIterations)   Mode  Cnt     Score       Error  Units
PrefixDoubleSequenceToStatsBenchmark.loopMapFilterToStats                      100000  thrpt    3  4612,798 ± 14043,377  ops/s
PrefixDoubleSequenceToStatsBenchmark.parallelStreamMapFilterToStats            100000  thrpt    3  9742,893 ±  3458,150  ops/s
PrefixDoubleSequenceToStatsBenchmark.sequenceMapFilterToStats                  100000  thrpt    3  2546,276 ±   264,243  ops/s
PrefixDoubleSequenceToStatsBenchmark.streamMapFilterToStats                    100000  thrpt    3  4864,121 ±   875,511  ops/s

Process finished with exit code 0

````
