````
"C:\Program Files\Java\jdk-17\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=56621:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin" -Dfile.encoding=UTF-8 -classpath C:\_Programming\Java\hzt-utils\benchmark\target\classes;C:\_Programming\Java\hzt-utils\core\target\classes;C:\Users\hzuiderv\.m2\repository\org\jetbrains\annotations\22.0.0\annotations-22.0.0.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-core\1.34\jmh-core-1.34.jar;C:\Users\hzuiderv\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\hzuiderv\.m2\repository\org\apache\commons\commons-math3\3.2\commons-math3-3.2.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.34\jmh-generator-annprocess-1.34.jar benchmark.prefix.DoubleTimSortBenchmark
# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=56621:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.DoubleTimSortBenchmark.arraySort
# Parameters: (nrOfIterations = 100000)

# Run progress: 0,00% complete, ETA 00:08:20
# Fork: 1 of 2
# Warmup Iteration   1: 146,571 ops/s
# Warmup Iteration   2: 150,104 ops/s
Iteration   1: 147,781 ops/s
Iteration   2: 151,846 ops/s
Iteration   3: 151,163 ops/s

# Run progress: 10,00% complete, ETA 00:07:40
# Fork: 2 of 2
# Warmup Iteration   1: 152,469 ops/s
# Warmup Iteration   2: 152,107 ops/s
Iteration   1: 152,719 ops/s
Iteration   2: 153,168 ops/s
Iteration   3: 153,023 ops/s


Result "benchmark.prefix.DoubleTimSortBenchmark.arraySort":
  151,617 ±(99.9%) 5,688 ops/s [Average]
  (min, avg, max) = (147,781, 151,617, 153,168), stdev = 2,028
  CI (99.9%): [145,928, 157,305] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=56621:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.DoubleTimSortBenchmark.doubleListSort
# Parameters: (nrOfIterations = 100000)

# Run progress: 20,00% complete, ETA 00:06:48
# Fork: 1 of 2
# Warmup Iteration   1: 150,592 ops/s
# Warmup Iteration   2: 154,455 ops/s
Iteration   1: 154,499 ops/s
Iteration   2: 154,442 ops/s
Iteration   3: 153,343 ops/s

# Run progress: 30,00% complete, ETA 00:05:57
# Fork: 2 of 2
# Warmup Iteration   1: 153,496 ops/s
# Warmup Iteration   2: 154,322 ops/s
Iteration   1: 155,661 ops/s
Iteration   2: 154,989 ops/s
Iteration   3: 155,318 ops/s


Result "benchmark.prefix.DoubleTimSortBenchmark.doubleListSort":
  154,709 ±(99.9%) 2,290 ops/s [Average]
  (min, avg, max) = (153,343, 154,709, 155,661), stdev = 0,817
  CI (99.9%): [152,418, 156,999] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=56621:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.DoubleTimSortBenchmark.doubleListSortReversed
# Parameters: (nrOfIterations = 100000)

# Run progress: 40,00% complete, ETA 00:05:06
# Fork: 1 of 2
# Warmup Iteration   1: 74,093 ops/s
# Warmup Iteration   2: 75,033 ops/s
Iteration   1: 74,609 ops/s
Iteration   2: 74,664 ops/s
Iteration   3: 74,015 ops/s

# Run progress: 50,00% complete, ETA 00:04:15
# Fork: 2 of 2
# Warmup Iteration   1: 73,012 ops/s
# Warmup Iteration   2: 68,755 ops/s
Iteration   1: 72,587 ops/s
Iteration   2: 73,564 ops/s
Iteration   3: 74,243 ops/s


Result "benchmark.prefix.DoubleTimSortBenchmark.doubleListSortReversed":
  73,947 ±(99.9%) 2,187 ops/s [Average]
  (min, avg, max) = (72,587, 73,947, 74,664), stdev = 0,780
  CI (99.9%): [71,760, 76,134] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=56621:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.DoubleTimSortBenchmark.listSort
# Parameters: (nrOfIterations = 100000)

# Run progress: 60,00% complete, ETA 00:03:24
# Fork: 1 of 2
# Warmup Iteration   1: 47,932 ops/s
# Warmup Iteration   2: 47,675 ops/s
Iteration   1: 48,640 ops/s
Iteration   2: 48,654 ops/s
Iteration   3: 47,915 ops/s

# Run progress: 70,00% complete, ETA 00:02:33
# Fork: 2 of 2
# Warmup Iteration   1: 47,969 ops/s
# Warmup Iteration   2: 47,657 ops/s
Iteration   1: 48,641 ops/s
Iteration   2: 48,551 ops/s
Iteration   3: 48,674 ops/s


Result "benchmark.prefix.DoubleTimSortBenchmark.listSort":
  48,513 ±(99.9%) 0,829 ops/s [Average]
  (min, avg, max) = (47,915, 48,513, 48,674), stdev = 0,296
  CI (99.9%): [47,684, 49,341] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=56621:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.DoubleTimSortBenchmark.listSortReversed
# Parameters: (nrOfIterations = 100000)

# Run progress: 80,00% complete, ETA 00:01:42
# Fork: 1 of 2
# Warmup Iteration   1: 43,106 ops/s
# Warmup Iteration   2: 44,136 ops/s
Iteration   1: 44,189 ops/s
Iteration   2: 44,443 ops/s
Iteration   3: 44,073 ops/s

# Run progress: 90,00% complete, ETA 00:00:51
# Fork: 2 of 2
# Warmup Iteration   1: 43,390 ops/s
# Warmup Iteration   2: 43,760 ops/s
Iteration   1: 44,267 ops/s
Iteration   2: 44,502 ops/s
Iteration   3: 44,577 ops/s


Result "benchmark.prefix.DoubleTimSortBenchmark.listSortReversed":
  44,342 ±(99.9%) 0,550 ops/s [Average]
  (min, avg, max) = (44,073, 44,342, 44,577), stdev = 0,196
  CI (99.9%): [43,792, 44,892] (assumes normal distribution)


# Run complete. Total time: 00:08:31

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

Benchmark                                      (nrOfIterations)   Mode  Cnt    Score   Error  Units
DoubleTimSortBenchmark.arraySort                         100000  thrpt    6  151,617 ± 5,688  ops/s
DoubleTimSortBenchmark.doubleListSort                    100000  thrpt    6  154,709 ± 2,290  ops/s
DoubleTimSortBenchmark.doubleListSortReversed            100000  thrpt    6   73,947 ± 2,187  ops/s
DoubleTimSortBenchmark.listSort                          100000  thrpt    6   48,513 ± 0,829  ops/s
DoubleTimSortBenchmark.listSortReversed                  100000  thrpt    6   44,342 ± 0,550  ops/s

Process finished with exit code 0
````
