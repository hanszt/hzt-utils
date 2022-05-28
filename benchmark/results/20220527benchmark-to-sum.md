````
"C:\Program Files\Java\jdk-17\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=56176:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin" -Dfile.encoding=UTF-8 -classpath C:\_Programming\Java\hzt-utils\benchmark\target\classes;C:\_Programming\Java\hzt-utils\core\target\classes;C:\Users\hzuiderv\.m2\repository\org\jetbrains\annotations\22.0.0\annotations-22.0.0.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-core\1.34\jmh-core-1.34.jar;C:\Users\hzuiderv\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\hzuiderv\.m2\repository\org\apache\commons\commons-math3\3.2\commons-math3-3.2.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.34\jmh-generator-annprocess-1.34.jar benchmark.prefix.PrefixIntRangeSumBenchmark
# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=56176:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixIntRangeSumBenchmark.intListXMapFilterSum
# Parameters: (nrOfIterations = 100000)

# Run progress: 0,00% complete, ETA 00:04:10
# Fork: 1 of 1
# Warmup Iteration   1: 1548,735 ops/s
# Warmup Iteration   2: 1660,911 ops/s
Iteration   1: 1681,404 ops/s
Iteration   2: 1649,672 ops/s
Iteration   3: 1602,320 ops/s


Result "benchmark.prefix.PrefixIntRangeSumBenchmark.intListXMapFilterSum":
  1644,466 ±(99.9%) 726,069 ops/s [Average]
  (min, avg, max) = (1602,320, 1644,466, 1681,404), stdev = 39,798
  CI (99.9%): [918,396, 2370,535] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=56176:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixIntRangeSumBenchmark.intSequenceMapFilterSum
# Parameters: (nrOfIterations = 100000)

# Run progress: 20,00% complete, ETA 00:03:24
# Fork: 1 of 1
# Warmup Iteration   1: 9535,201 ops/s
# Warmup Iteration   2: 9451,271 ops/s
Iteration   1: 9530,071 ops/s
Iteration   2: 9580,198 ops/s
Iteration   3: 9577,472 ops/s


Result "benchmark.prefix.PrefixIntRangeSumBenchmark.intSequenceMapFilterSum":
  9562,580 ±(99.9%) 514,233 ops/s [Average]
  (min, avg, max) = (9530,071, 9562,580, 9580,198), stdev = 28,187
  CI (99.9%): [9048,347, 10076,814] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=56176:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixIntRangeSumBenchmark.loopMapFilterSum
# Parameters: (nrOfIterations = 100000)

# Run progress: 40,00% complete, ETA 00:02:33
# Fork: 1 of 1
# Warmup Iteration   1: 20182,228 ops/s
# Warmup Iteration   2: 19881,928 ops/s
Iteration   1: 18680,704 ops/s
Iteration   2: 18760,983 ops/s
Iteration   3: 18729,340 ops/s


Result "benchmark.prefix.PrefixIntRangeSumBenchmark.loopMapFilterSum":
  18723,676 ±(99.9%) 737,743 ops/s [Average]
  (min, avg, max) = (18680,704, 18723,676, 18760,983), stdev = 40,438
  CI (99.9%): [17985,933, 19461,418] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=56176:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixIntRangeSumBenchmark.parallelStreamMapFilterSum
# Parameters: (nrOfIterations = 100000)

# Run progress: 60,00% complete, ETA 00:01:42
# Fork: 1 of 1
# Warmup Iteration   1: 22566,713 ops/s
# Warmup Iteration   2: 22856,398 ops/s
Iteration   1: 22476,502 ops/s
Iteration   2: 22369,527 ops/s
Iteration   3: 21893,423 ops/s


Result "benchmark.prefix.PrefixIntRangeSumBenchmark.parallelStreamMapFilterSum":
  22246,484 ±(99.9%) 5662,910 ops/s [Average]
  (min, avg, max) = (21893,423, 22246,484, 22476,502), stdev = 310,403
  CI (99.9%): [16583,574, 27909,395] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=56176:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixIntRangeSumBenchmark.streamMapFilterSum
# Parameters: (nrOfIterations = 100000)

# Run progress: 80,00% complete, ETA 00:00:51
# Fork: 1 of 1
# Warmup Iteration   1: 8917,887 ops/s
# Warmup Iteration   2: 9067,911 ops/s
Iteration   1: 9001,543 ops/s
Iteration   2: 8550,826 ops/s
Iteration   3: 8290,875 ops/s


Result "benchmark.prefix.PrefixIntRangeSumBenchmark.streamMapFilterSum":
  8614,415 ±(99.9%) 6559,999 ops/s [Average]
  (min, avg, max) = (8290,875, 8614,415, 9001,543), stdev = 359,576
  CI (99.9%): [2054,416, 15174,413] (assumes normal distribution)


# Run complete. Total time: 00:04:15

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

Benchmark                                              (nrOfIterations)   Mode  Cnt      Score      Error  Units
PrefixIntRangeSumBenchmark.intListXMapFilterSum                  100000  thrpt    3   1644,466 ±  726,069  ops/s
PrefixIntRangeSumBenchmark.intSequenceMapFilterSum               100000  thrpt    3   9562,580 ±  514,233  ops/s
PrefixIntRangeSumBenchmark.loopMapFilterSum                      100000  thrpt    3  18723,676 ±  737,743  ops/s
PrefixIntRangeSumBenchmark.parallelStreamMapFilterSum            100000  thrpt    3  22246,484 ± 5662,910  ops/s
PrefixIntRangeSumBenchmark.streamMapFilterSum                    100000  thrpt    3   8614,415 ± 6559,999  ops/s

Process finished with exit code 0

````
