````
"C:\Program Files\Java\jdk-17\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=54738:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin" -Dfile.encoding=UTF-8 -classpath C:\_Programming\Java\hzt-utils\benchmark\target\classes;C:\_Programming\Java\hzt-utils\core\target\classes;C:\Users\hzuiderv\.m2\repository\org\jetbrains\annotations\22.0.0\annotations-22.0.0.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-core\1.34\jmh-core-1.34.jar;C:\Users\hzuiderv\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\hzuiderv\.m2\repository\org\apache\commons\commons-math3\3.2\commons-math3-3.2.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.34\jmh-generator-annprocess-1.34.jar benchmark.prefix.PrefixIntRangeToArrayBenchmark
# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=54738:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixIntRangeToArrayBenchmark.intRangeMapFilterToArray
# Parameters: (nrOfIterations = 100000)

# Run progress: 0,00% complete, ETA 00:06:40
# Fork: 1 of 2
# Warmup Iteration   1: 3744,687 ops/s
# Warmup Iteration   2: 3820,930 ops/s
Iteration   1: 3767,302 ops/s
Iteration   2: 3938,433 ops/s
Iteration   3: 3979,513 ops/s

# Run progress: 12,50% complete, ETA 00:05:57
# Fork: 2 of 2
# Warmup Iteration   1: 3732,144 ops/s
# Warmup Iteration   2: 3872,203 ops/s
Iteration   1: 3861,770 ops/s
Iteration   2: 3956,211 ops/s
Iteration   3: 3928,725 ops/s


Result "benchmark.prefix.PrefixIntRangeToArrayBenchmark.intRangeMapFilterToArray":
  3905,326 ±(99.9%) 219,675 ops/s [Average]
  (min, avg, max) = (3767,302, 3905,326, 3979,513), stdev = 78,338
  CI (99.9%): [3685,651, 4125,000] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=54738:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixIntRangeToArrayBenchmark.loopMapFilterToArray
# Parameters: (nrOfIterations = 100000)

# Run progress: 25,00% complete, ETA 00:05:06
# Fork: 1 of 2
# Warmup Iteration   1: 16711,423 ops/s
# Warmup Iteration   2: 16556,924 ops/s
Iteration   1: 16016,705 ops/s
Iteration   2: 14476,703 ops/s
Iteration   3: 15614,637 ops/s

# Run progress: 37,50% complete, ETA 00:04:14
# Fork: 2 of 2
# Warmup Iteration   1: 16409,246 ops/s
# Warmup Iteration   2: 15928,101 ops/s
Iteration   1: 16117,409 ops/s
Iteration   2: 15626,385 ops/s
Iteration   3: 14174,146 ops/s


Result "benchmark.prefix.PrefixIntRangeToArrayBenchmark.loopMapFilterToArray":
  15337,664 ±(99.9%) 2286,470 ops/s [Average]
  (min, avg, max) = (14174,146, 15337,664, 16117,409), stdev = 815,377
  CI (99.9%): [13051,194, 17624,134] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=54738:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixIntRangeToArrayBenchmark.parallelStreamMapFilterToArray
# Parameters: (nrOfIterations = 100000)

# Run progress: 50,00% complete, ETA 00:03:23
# Fork: 1 of 2
# Warmup Iteration   1: 9216,134 ops/s
# Warmup Iteration   2: 9652,242 ops/s
Iteration   1: 9512,723 ops/s
Iteration   2: 9454,902 ops/s
Iteration   3: 9128,107 ops/s

# Run progress: 62,50% complete, ETA 00:02:32
# Fork: 2 of 2
# Warmup Iteration   1: 6851,391 ops/s
# Warmup Iteration   2: 7276,484 ops/s
Iteration   1: 6656,104 ops/s
Iteration   2: 7296,928 ops/s
Iteration   3: 7298,167 ops/s


Result "benchmark.prefix.PrefixIntRangeToArrayBenchmark.parallelStreamMapFilterToArray":
  8224,489 ±(99.9%) 3584,155 ops/s [Average]
  (min, avg, max) = (6656,104, 8224,489, 9512,723), stdev = 1278,144
  CI (99.9%): [4640,334, 11808,643] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=54738:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixIntRangeToArrayBenchmark.streamMapFilterToArray
# Parameters: (nrOfIterations = 100000)

# Run progress: 75,00% complete, ETA 00:01:41
# Fork: 1 of 2
# Warmup Iteration   1: 3190,567 ops/s
# Warmup Iteration   2: 3108,438 ops/s
Iteration   1: 3143,842 ops/s
Iteration   2: 3166,275 ops/s
Iteration   3: 3212,229 ops/s

# Run progress: 87,50% complete, ETA 00:00:50
# Fork: 2 of 2
# Warmup Iteration   1: 3303,082 ops/s
# Warmup Iteration   2: 3251,270 ops/s
Iteration   1: 3578,519 ops/s
Iteration   2: 3609,494 ops/s
Iteration   3: 3676,754 ops/s


Result "benchmark.prefix.PrefixIntRangeToArrayBenchmark.streamMapFilterToArray":
  3397,852 ±(99.9%) 695,782 ops/s [Average]
  (min, avg, max) = (3143,842, 3397,852, 3676,754), stdev = 248,123
  CI (99.9%): [2702,070, 4093,634] (assumes normal distribution)


# Run complete. Total time: 00:06:47

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

Benchmark                                                      (nrOfIterations)   Mode  Cnt      Score      Error  Units
PrefixIntRangeToArrayBenchmark.intRangeMapFilterToArray                  100000  thrpt    6   3905,326 ±  219,675  ops/s
PrefixIntRangeToArrayBenchmark.loopMapFilterToArray                      100000  thrpt    6  15337,664 ± 2286,470  ops/s
PrefixIntRangeToArrayBenchmark.parallelStreamMapFilterToArray            100000  thrpt    6   8224,489 ± 3584,155  ops/s
PrefixIntRangeToArrayBenchmark.streamMapFilterToArray                    100000  thrpt    6   3397,852 ±  695,782  ops/s

Process finished with exit code 0

````
