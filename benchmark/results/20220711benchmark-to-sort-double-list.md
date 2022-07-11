````
"C:\Program Files\Java\jdk-11\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=63424:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin" -Dfile.encoding=UTF-8 -classpath C:\_Programming\Java\hzt-utils\benchmark\target\classes;C:\_Programming\Java\hzt-utils\core\target\classes;C:\Users\hzuiderv\.m2\repository\org\jetbrains\annotations\22.0.0\annotations-22.0.0.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-core\1.35\jmh-core-1.35.jar;C:\Users\hzuiderv\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\hzuiderv\.m2\repository\org\apache\commons\commons-math3\3.2\commons-math3-3.2.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.35\jmh-generator-annprocess-1.35.jar benchmark.prefix.DoubleTimSortBenchmark
# JMH version: 1.35
# VM version: JDK 11.0.13, Java HotSpot(TM) 64-Bit Server VM, 11.0.13+10-LTS-370
# VM invoker: C:\Program Files\Java\jdk-11\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=63424:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.DoubleTimSortBenchmark.arraySort
# Parameters: (nrOfIterations = 100000)

# Run progress: 0,00% complete, ETA 00:10:00
# Fork: 1 of 2
# Warmup Iteration   1: 109,980 ops/s
# Warmup Iteration   2: 112,679 ops/s
Iteration   1: 112,323 ops/s
Iteration   2: 112,306 ops/s
Iteration   3: 112,777 ops/s

# Run progress: 8,33% complete, ETA 00:09:24
# Fork: 2 of 2
# Warmup Iteration   1: 103,251 ops/s
# Warmup Iteration   2: 104,548 ops/s
Iteration   1: 106,783 ops/s
Iteration   2: 105,766 ops/s
Iteration   3: 106,784 ops/s


Result "benchmark.prefix.DoubleTimSortBenchmark.arraySort":
  109,456 ±(99.9%) 9,323 ops/s [Average]
  (min, avg, max) = (105,766, 109,456, 112,777), stdev = 3,325
  CI (99.9%): [100,133, 118,779] (assumes normal distribution)


# JMH version: 1.35
# VM version: JDK 11.0.13, Java HotSpot(TM) 64-Bit Server VM, 11.0.13+10-LTS-370
# VM invoker: C:\Program Files\Java\jdk-11\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=63424:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.DoubleTimSortBenchmark.doubleListSort
# Parameters: (nrOfIterations = 100000)

# Run progress: 16,67% complete, ETA 00:08:32
# Fork: 1 of 2
# Warmup Iteration   1: 106,800 ops/s
# Warmup Iteration   2: 108,100 ops/s
Iteration   1: 108,517 ops/s
Iteration   2: 107,532 ops/s
Iteration   3: 107,294 ops/s

# Run progress: 25,00% complete, ETA 00:07:40
# Fork: 2 of 2
# Warmup Iteration   1: 88,065 ops/s
# Warmup Iteration   2: 90,045 ops/s
Iteration   1: 91,315 ops/s
Iteration   2: 91,959 ops/s
Iteration   3: 91,761 ops/s


Result "benchmark.prefix.DoubleTimSortBenchmark.doubleListSort":
  99,730 ±(99.9%) 24,766 ops/s [Average]
  (min, avg, max) = (91,315, 99,730, 108,517), stdev = 8,832
  CI (99.9%): [74,964, 124,496] (assumes normal distribution)


# JMH version: 1.35
# VM version: JDK 11.0.13, Java HotSpot(TM) 64-Bit Server VM, 11.0.13+10-LTS-370
# VM invoker: C:\Program Files\Java\jdk-11\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=63424:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.DoubleTimSortBenchmark.doubleListSortReversed
# Parameters: (nrOfIterations = 100000)

# Run progress: 33,33% complete, ETA 00:06:49
# Fork: 1 of 2
# Warmup Iteration   1: 68,206 ops/s
# Warmup Iteration   2: 70,467 ops/s
Iteration   1: 69,467 ops/s
Iteration   2: 69,580 ops/s
Iteration   3: 70,303 ops/s

# Run progress: 41,67% complete, ETA 00:05:58
# Fork: 2 of 2
# Warmup Iteration   1: 69,539 ops/s
# Warmup Iteration   2: 70,760 ops/s
Iteration   1: 71,761 ops/s
Iteration   2: 71,740 ops/s
Iteration   3: 71,690 ops/s


Result "benchmark.prefix.DoubleTimSortBenchmark.doubleListSortReversed":
  70,757 ±(99.9%) 3,097 ops/s [Average]
  (min, avg, max) = (69,467, 70,757, 71,761), stdev = 1,105
  CI (99.9%): [67,659, 73,854] (assumes normal distribution)


# JMH version: 1.35
# VM version: JDK 11.0.13, Java HotSpot(TM) 64-Bit Server VM, 11.0.13+10-LTS-370
# VM invoker: C:\Program Files\Java\jdk-11\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=63424:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.DoubleTimSortBenchmark.listSort
# Parameters: (nrOfIterations = 100000)

# Run progress: 50,00% complete, ETA 00:05:06
# Fork: 1 of 2
# Warmup Iteration   1: 34,555 ops/s
# Warmup Iteration   2: 34,823 ops/s
Iteration   1: 34,657 ops/s
Iteration   2: 34,915 ops/s
Iteration   3: 34,598 ops/s

# Run progress: 58,33% complete, ETA 00:04:15
# Fork: 2 of 2
# Warmup Iteration   1: 33,378 ops/s
# Warmup Iteration   2: 34,125 ops/s
Iteration   1: 34,669 ops/s
Iteration   2: 33,700 ops/s
Iteration   3: 33,249 ops/s


Result "benchmark.prefix.DoubleTimSortBenchmark.listSort":
  34,298 ±(99.9%) 1,858 ops/s [Average]
  (min, avg, max) = (33,249, 34,298, 34,915), stdev = 0,663
  CI (99.9%): [32,440, 36,156] (assumes normal distribution)


# JMH version: 1.35
# VM version: JDK 11.0.13, Java HotSpot(TM) 64-Bit Server VM, 11.0.13+10-LTS-370
# VM invoker: C:\Program Files\Java\jdk-11\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=63424:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.DoubleTimSortBenchmark.listSortReversed
# Parameters: (nrOfIterations = 100000)

# Run progress: 66,67% complete, ETA 00:03:24
# Fork: 1 of 2
# Warmup Iteration   1: 33,738 ops/s
# Warmup Iteration   2: 34,137 ops/s
Iteration   1: 34,439 ops/s
Iteration   2: 34,026 ops/s
Iteration   3: 33,951 ops/s

# Run progress: 75,00% complete, ETA 00:02:33
# Fork: 2 of 2
# Warmup Iteration   1: 33,370 ops/s
# Warmup Iteration   2: 33,820 ops/s
Iteration   1: 33,499 ops/s
Iteration   2: 34,096 ops/s
Iteration   3: 33,243 ops/s


Result "benchmark.prefix.DoubleTimSortBenchmark.listSortReversed":
  33,876 ±(99.9%) 1,214 ops/s [Average]
  (min, avg, max) = (33,243, 33,876, 34,439), stdev = 0,433
  CI (99.9%): [32,662, 35,089] (assumes normal distribution)


# JMH version: 1.35
# VM version: JDK 11.0.13, Java HotSpot(TM) 64-Bit Server VM, 11.0.13+10-LTS-370
# VM invoker: C:\Program Files\Java\jdk-11\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=63424:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.DoubleTimSortBenchmark.primitiveArraySortReversed
# Parameters: (nrOfIterations = 100000)

# Run progress: 83,33% complete, ETA 00:01:42
# Fork: 1 of 2
# Warmup Iteration   1: 64,686 ops/s
# Warmup Iteration   2: 66,289 ops/s
Iteration   1: 69,288 ops/s
Iteration   2: 69,407 ops/s
Iteration   3: 69,817 ops/s

# Run progress: 91,67% complete, ETA 00:00:51
# Fork: 2 of 2
# Warmup Iteration   1: 65,994 ops/s
# Warmup Iteration   2: 67,976 ops/s
Iteration   1: 70,615 ops/s
Iteration   2: 70,352 ops/s
Iteration   3: 70,608 ops/s


Result "benchmark.prefix.DoubleTimSortBenchmark.primitiveArraySortReversed":
  70,014 ±(99.9%) 1,666 ops/s [Average]
  (min, avg, max) = (69,288, 70,014, 70,615), stdev = 0,594
  CI (99.9%): [68,349, 71,680] (assumes normal distribution)


# Run complete. Total time: 00:10:13

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

Benchmark                                          (nrOfIterations)   Mode  Cnt    Score    Error  Units
DoubleTimSortBenchmark.arraySort                             100000  thrpt    6  109,456 ±  9,323  ops/s
DoubleTimSortBenchmark.doubleListSort                        100000  thrpt    6   99,730 ± 24,766  ops/s
DoubleTimSortBenchmark.doubleListSortReversed                100000  thrpt    6   70,757 ±  3,097  ops/s
DoubleTimSortBenchmark.listSort                              100000  thrpt    6   34,298 ±  1,858  ops/s
DoubleTimSortBenchmark.listSortReversed                      100000  thrpt    6   33,876 ±  1,214  ops/s
DoubleTimSortBenchmark.primitiveArraySortReversed            100000  thrpt    6   70,014 ±  1,666  ops/s

Process finished with exit code 0

````
