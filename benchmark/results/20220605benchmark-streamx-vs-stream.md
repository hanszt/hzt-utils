````
"C:\Program Files\Java\jdk-11\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=51414:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin" -Dfile.encoding=UTF-8 -classpath C:\_Programming\Java\hzt-utils\benchmark\target\classes;C:\_Programming\Java\hzt-utils\core\target\classes;C:\Users\hzuiderv\.m2\repository\org\jetbrains\annotations\22.0.0\annotations-22.0.0.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-core\1.34\jmh-core-1.34.jar;C:\Users\hzuiderv\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\hzuiderv\.m2\repository\org\apache\commons\commons-math3\3.2\commons-math3-3.2.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.34\jmh-generator-annprocess-1.34.jar benchmark.prefix.PrefixStreamXMapFilterToListBenchmark
# JMH version: 1.34
# VM version: JDK 11.0.13, Java HotSpot(TM) 64-Bit Server VM, 11.0.13+10-LTS-370
# VM invoker: C:\Program Files\Java\jdk-11\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=51414:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixStreamXMapFilterToListBenchmark.imperativeMapFilterToList
# Parameters: (nrOfIterations = 1000000)

# Run progress: 0,00% complete, ETA 00:08:20
# Fork: 1 of 2
# Warmup Iteration   1: 43,854 ops/s
# Warmup Iteration   2: 47,669 ops/s
Iteration   1: 50,327 ops/s
Iteration   2: 50,868 ops/s
Iteration   3: 50,952 ops/s

# Run progress: 10,00% complete, ETA 00:07:43
# Fork: 2 of 2
# Warmup Iteration   1: 45,803 ops/s
# Warmup Iteration   2: 48,917 ops/s
Iteration   1: 51,158 ops/s
Iteration   2: 50,867 ops/s
Iteration   3: 51,422 ops/s


Result "benchmark.prefix.PrefixStreamXMapFilterToListBenchmark.imperativeMapFilterToList":
  50,932 ±(99.9%) 1,024 ops/s [Average]
  (min, avg, max) = (50,327, 50,932, 51,422), stdev = 0,365
  CI (99.9%): [49,909, 51,956] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 11.0.13, Java HotSpot(TM) 64-Bit Server VM, 11.0.13+10-LTS-370
# VM invoker: C:\Program Files\Java\jdk-11\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=51414:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixStreamXMapFilterToListBenchmark.parallelStreamMapFilterToList
# Parameters: (nrOfIterations = 1000000)

# Run progress: 20,00% complete, ETA 00:06:50
# Fork: 1 of 2
# Warmup Iteration   1: 68,408 ops/s
# Warmup Iteration   2: 72,642 ops/s
Iteration   1: 73,883 ops/s
Iteration   2: 75,137 ops/s
Iteration   3: 74,992 ops/s

# Run progress: 30,00% complete, ETA 00:05:59
# Fork: 2 of 2
# Warmup Iteration   1: 68,975 ops/s
# Warmup Iteration   2: 72,062 ops/s
Iteration   1: 70,610 ops/s
Iteration   2: 71,038 ops/s
Iteration   3: 71,707 ops/s


Result "benchmark.prefix.PrefixStreamXMapFilterToListBenchmark.parallelStreamMapFilterToList":
  72,894 ±(99.9%) 5,675 ops/s [Average]
  (min, avg, max) = (70,610, 72,894, 75,137), stdev = 2,024
  CI (99.9%): [67,219, 78,570] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 11.0.13, Java HotSpot(TM) 64-Bit Server VM, 11.0.13+10-LTS-370
# VM invoker: C:\Program Files\Java\jdk-11\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=51414:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixStreamXMapFilterToListBenchmark.parallelStreamXMapFilterToList
# Parameters: (nrOfIterations = 1000000)

# Run progress: 40,00% complete, ETA 00:05:08
# Fork: 1 of 2
# Warmup Iteration   1: 64,771 ops/s
# Warmup Iteration   2: 71,040 ops/s
Iteration   1: 71,193 ops/s
Iteration   2: 71,754 ops/s
Iteration   3: 73,492 ops/s

# Run progress: 50,00% complete, ETA 00:04:16
# Fork: 2 of 2
# Warmup Iteration   1: 67,706 ops/s
# Warmup Iteration   2: 71,935 ops/s
Iteration   1: 72,705 ops/s
Iteration   2: 73,236 ops/s
Iteration   3: 73,641 ops/s


Result "benchmark.prefix.PrefixStreamXMapFilterToListBenchmark.parallelStreamXMapFilterToList":
  72,670 ±(99.9%) 2,793 ops/s [Average]
  (min, avg, max) = (71,193, 72,670, 73,641), stdev = 0,996
  CI (99.9%): [69,877, 75,463] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 11.0.13, Java HotSpot(TM) 64-Bit Server VM, 11.0.13+10-LTS-370
# VM invoker: C:\Program Files\Java\jdk-11\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=51414:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixStreamXMapFilterToListBenchmark.streamMapFilterToList
# Parameters: (nrOfIterations = 1000000)

# Run progress: 60,00% complete, ETA 00:03:25
# Fork: 1 of 2
# Warmup Iteration   1: 43,862 ops/s
# Warmup Iteration   2: 46,528 ops/s
Iteration   1: 48,114 ops/s
Iteration   2: 48,285 ops/s
Iteration   3: 46,989 ops/s

# Run progress: 70,00% complete, ETA 00:02:33
# Fork: 2 of 2
# Warmup Iteration   1: 39,886 ops/s
# Warmup Iteration   2: 45,202 ops/s
Iteration   1: 46,085 ops/s
Iteration   2: 46,907 ops/s
Iteration   3: 47,729 ops/s


Result "benchmark.prefix.PrefixStreamXMapFilterToListBenchmark.streamMapFilterToList":
  47,351 ±(99.9%) 2,356 ops/s [Average]
  (min, avg, max) = (46,085, 47,351, 48,285), stdev = 0,840
  CI (99.9%): [44,995, 49,708] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 11.0.13, Java HotSpot(TM) 64-Bit Server VM, 11.0.13+10-LTS-370
# VM invoker: C:\Program Files\Java\jdk-11\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=51414:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixStreamXMapFilterToListBenchmark.streamXMapFilterToList
# Parameters: (nrOfIterations = 1000000)

# Run progress: 80,00% complete, ETA 00:01:42
# Fork: 1 of 2
# Warmup Iteration   1: 38,804 ops/s
# Warmup Iteration   2: 41,961 ops/s
Iteration   1: 43,720 ops/s
Iteration   2: 44,278 ops/s
Iteration   3: 43,985 ops/s

# Run progress: 90,00% complete, ETA 00:00:51
# Fork: 2 of 2
# Warmup Iteration   1: 38,529 ops/s
# Warmup Iteration   2: 42,513 ops/s
Iteration   1: 43,398 ops/s
Iteration   2: 44,014 ops/s
Iteration   3: 43,635 ops/s


Result "benchmark.prefix.PrefixStreamXMapFilterToListBenchmark.streamXMapFilterToList":
  43,838 ±(99.9%) 0,882 ops/s [Average]
  (min, avg, max) = (43,398, 43,838, 44,278), stdev = 0,315
  CI (99.9%): [42,956, 44,721] (assumes normal distribution)


# Run complete. Total time: 00:08:32

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

Benchmark                                                             (nrOfIterations)   Mode  Cnt   Score   Error  Units
PrefixStreamXMapFilterToListBenchmark.imperativeMapFilterToList                1000000  thrpt    6  50,932 ± 1,024  ops/s
PrefixStreamXMapFilterToListBenchmark.parallelStreamMapFilterToList            1000000  thrpt    6  72,894 ± 5,675  ops/s
PrefixStreamXMapFilterToListBenchmark.parallelStreamXMapFilterToList           1000000  thrpt    6  72,670 ± 2,793  ops/s
PrefixStreamXMapFilterToListBenchmark.streamMapFilterToList                    1000000  thrpt    6  47,351 ± 2,356  ops/s
PrefixStreamXMapFilterToListBenchmark.streamXMapFilterToList                   1000000  thrpt    6  43,838 ± 0,882  ops/s

Process finished with exit code 0
````
