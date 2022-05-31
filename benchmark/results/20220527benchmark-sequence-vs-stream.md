## Run 1 at around 14:00:00
```
"C:\Program Files\Java\jdk-17\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=55067:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin" -Dfile.encoding=UTF-8 -classpath C:\_Programming\Java\hzt-utils\benchmark\target\classes;C:\_Programming\Java\hzt-utils\core\target\classes;C:\Users\hzuiderv\.m2\repository\org\jetbrains\annotations\22.0.0\annotations-22.0.0.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-core\1.34\jmh-core-1.34.jar;C:\Users\hzuiderv\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\hzuiderv\.m2\repository\org\apache\commons\commons-math3\3.2\commons-math3-3.2.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.34\jmh-generator-annprocess-1.34.jar benchmark.prefix.PrefixSequenceMapFilterToListBenchmark
# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=55067:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 5 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixSequenceMapFilterToListBenchmark.sequenceOfListMapFilterToList
# Parameters: (nrOfIterations = 100000)

# Run progress: 0,00% complete, ETA 00:16:40
# Fork: 1 of 5
# Warmup Iteration   1: 1903,087 ops/s
# Warmup Iteration   2: 1847,861 ops/s
# Warmup Iteration   3: 1951,947 ops/s
# Warmup Iteration   4: 1887,087 ops/s
# Warmup Iteration   5: 2003,664 ops/s
Iteration   1: 1988,319 ops/s
Iteration   2: 1997,125 ops/s
Iteration   3: 1962,542 ops/s
Iteration   4: 1983,917 ops/s
Iteration   5: 1931,061 ops/s

# Run progress: 10,00% complete, ETA 00:15:09
# Fork: 2 of 5
# Warmup Iteration   1: 1971,106 ops/s
# Warmup Iteration   2: 1983,025 ops/s
# Warmup Iteration   3: 1991,397 ops/s
# Warmup Iteration   4: 1945,937 ops/s
# Warmup Iteration   5: 1981,948 ops/s
Iteration   1: 1935,662 ops/s
Iteration   2: 1959,886 ops/s
Iteration   3: 1981,437 ops/s
Iteration   4: 1985,493 ops/s
Iteration   5: 1766,938 ops/s

# Run progress: 20,00% complete, ETA 00:13:27
# Fork: 3 of 5
# Warmup Iteration   1: 1957,104 ops/s
# Warmup Iteration   2: 1940,951 ops/s
# Warmup Iteration   3: 1988,995 ops/s
# Warmup Iteration   4: 1129,511 ops/s
# Warmup Iteration   5: 1252,076 ops/s
Iteration   1: 1364,836 ops/s
Iteration   2: 1610,585 ops/s
Iteration   3: 1387,139 ops/s
Iteration   4: 1340,591 ops/s
Iteration   5: 1336,167 ops/s

# Run progress: 30,00% complete, ETA 00:11:46
# Fork: 4 of 5
# Warmup Iteration   1: 1493,901 ops/s
# Warmup Iteration   2: 1257,610 ops/s
# Warmup Iteration   3: 1437,324 ops/s
# Warmup Iteration   4: 1612,043 ops/s
# Warmup Iteration   5: 1846,220 ops/s
Iteration   1: 1863,513 ops/s
Iteration   2: 1832,066 ops/s
Iteration   3: 1825,969 ops/s
Iteration   4: 1803,431 ops/s
Iteration   5: 1927,795 ops/s

# Run progress: 40,00% complete, ETA 00:10:05
# Fork: 5 of 5
# Warmup Iteration   1: 1964,577 ops/s
# Warmup Iteration   2: 1979,105 ops/s
# Warmup Iteration   3: 1958,568 ops/s
# Warmup Iteration   4: 2009,888 ops/s
# Warmup Iteration   5: 1975,505 ops/s
Iteration   1: 1935,944 ops/s
Iteration   2: 1975,382 ops/s
Iteration   3: 1987,543 ops/s
Iteration   4: 1953,236 ops/s
Iteration   5: 1982,506 ops/s


Result "benchmark.prefix.PrefixSequenceMapFilterToListBenchmark.sequenceOfListMapFilterToList":
1824,763 ±(99.9%) 169,854 ops/s [Average]
(min, avg, max) = (1336,167, 1824,763, 1997,125), stdev = 226,751
CI (99.9%): [1654,909, 1994,618] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=55067:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 5 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixSequenceMapFilterToListBenchmark.streamMapFilterToList
# Parameters: (nrOfIterations = 100000)

# Run progress: 50,00% complete, ETA 00:08:24
# Fork: 1 of 5
# Warmup Iteration   1: 2276,987 ops/s
# Warmup Iteration   2: 2197,637 ops/s
# Warmup Iteration   3: 2258,555 ops/s
# Warmup Iteration   4: 2257,275 ops/s
# Warmup Iteration   5: 2248,724 ops/s
Iteration   1: 2256,049 ops/s
Iteration   2: 2240,703 ops/s
Iteration   3: 2194,515 ops/s
Iteration   4: 2255,304 ops/s
Iteration   5: 2262,673 ops/s

# Run progress: 60,00% complete, ETA 00:06:43
# Fork: 2 of 5
# Warmup Iteration   1: 2432,113 ops/s
# Warmup Iteration   2: 2409,195 ops/s
# Warmup Iteration   3: 2386,482 ops/s
# Warmup Iteration   4: 2355,702 ops/s
# Warmup Iteration   5: 2422,391 ops/s
Iteration   1: 2380,115 ops/s
Iteration   2: 2435,203 ops/s
Iteration   3: 2432,303 ops/s
Iteration   4: 2403,452 ops/s
Iteration   5: 2353,449 ops/s

# Run progress: 70,00% complete, ETA 00:05:02
# Fork: 3 of 5
# Warmup Iteration   1: 2497,924 ops/s
# Warmup Iteration   2: 2368,914 ops/s
# Warmup Iteration   3: 2362,892 ops/s
# Warmup Iteration   4: 2319,761 ops/s
# Warmup Iteration   5: 2322,171 ops/s
Iteration   1: 2303,529 ops/s
Iteration   2: 2339,572 ops/s
Iteration   3: 2369,379 ops/s
Iteration   4: 2364,539 ops/s
Iteration   5: 2353,997 ops/s

# Run progress: 80,00% complete, ETA 00:03:21
# Fork: 4 of 5
# Warmup Iteration   1: 2493,909 ops/s
# Warmup Iteration   2: 2209,270 ops/s
# Warmup Iteration   3: 2213,639 ops/s
# Warmup Iteration   4: 2244,739 ops/s
# Warmup Iteration   5: 2252,012 ops/s
Iteration   1: 2251,663 ops/s
Iteration   2: 2255,496 ops/s
Iteration   3: 2215,179 ops/s
Iteration   4: 2261,741 ops/s
Iteration   5: 2274,392 ops/s

# Run progress: 90,00% complete, ETA 00:01:40
# Fork: 5 of 5
# Warmup Iteration   1: 2541,358 ops/s
# Warmup Iteration   2: 2346,202 ops/s
# Warmup Iteration   3: 2352,399 ops/s
# Warmup Iteration   4: 2299,707 ops/s
# Warmup Iteration   5: 2358,142 ops/s
Iteration   1: 2325,016 ops/s
Iteration   2: 2348,209 ops/s
Iteration   3: 2359,875 ops/s
Iteration   4: 2345,624 ops/s
Iteration   5: 2320,819 ops/s


Result "benchmark.prefix.PrefixSequenceMapFilterToListBenchmark.streamMapFilterToList":
2316,112 ±(99.9%) 49,639 ops/s [Average]
(min, avg, max) = (2194,515, 2316,112, 2435,203), stdev = 66,267
CI (99.9%): [2266,473, 2365,751] (assumes normal distribution)


# Run complete. Total time: 00:16:49

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

Benchmark                                              (nrOfIterations)   Mode  Cnt     Score     Error  Units
PrefixSequenceBenchmark.sequenceOfListMapFilterToList            100000  thrpt   25  1824,763 ± 169,854  ops/s
PrefixSequenceBenchmark.streamMapFilterToList                    100000  thrpt   25  2316,112 ±  49,639  ops/s

Process finished with exit code 0
```

## Run 2 at around 16:30 
````
"C:\Program Files\Java\jdk-17\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=50853:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin" -Dfile.encoding=UTF-8 -classpath C:\_Programming\Java\hzt-utils\benchmark\target\classes;C:\_Programming\Java\hzt-utils\core\target\classes;C:\Users\hzuiderv\.m2\repository\org\jetbrains\annotations\22.0.0\annotations-22.0.0.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-core\1.34\jmh-core-1.34.jar;C:\Users\hzuiderv\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\hzuiderv\.m2\repository\org\apache\commons\commons-math3\3.2\commons-math3-3.2.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.34\jmh-generator-annprocess-1.34.jar benchmark.prefix.PrefixSequenceMapFilterToListBenchmark
# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=50853:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 1 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixSequenceMapFilterToListBenchmark.imperativeMapFilterToList
# Parameters: (nrOfIterations = 100000)

# Run progress: 0,00% complete, ETA 00:05:20
# Fork: 1 of 2
# Warmup Iteration   1: 3904,816 ops/s
Iteration   1: 3944,098 ops/s
Iteration   2: 3960,598 ops/s
Iteration   3: 3876,322 ops/s

# Run progress: 12,50% complete, ETA 00:04:48
# Fork: 2 of 2
# Warmup Iteration   1: 4060,501 ops/s
Iteration   1: 4053,524 ops/s
Iteration   2: 4031,938 ops/s
Iteration   3: 3765,534 ops/s


Result "benchmark.prefix.PrefixSequenceMapFilterToListBenchmark.imperativeMapFilterToList":
  3938,669 ±(99.9%) 297,466 ops/s [Average]
  (min, avg, max) = (3765,534, 3938,669, 4053,524), stdev = 106,079
  CI (99.9%): [3641,203, 4236,135] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=50853:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 1 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixSequenceMapFilterToListBenchmark.parallelStreamMapFilterToList
# Parameters: (nrOfIterations = 100000)

# Run progress: 25,00% complete, ETA 00:04:06
# Fork: 1 of 2
# Warmup Iteration   1: 8035,181 ops/s
Iteration   1: 7957,365 ops/s
Iteration   2: 7697,550 ops/s
Iteration   3: 7141,911 ops/s

# Run progress: 37,50% complete, ETA 00:03:25
# Fork: 2 of 2
# Warmup Iteration   1: 7646,865 ops/s
Iteration   1: 7606,613 ops/s
Iteration   2: 7440,504 ops/s
Iteration   3: 7609,385 ops/s


Result "benchmark.prefix.PrefixSequenceMapFilterToListBenchmark.parallelStreamMapFilterToList":
  7575,555 ±(99.9%) 761,848 ops/s [Average]
  (min, avg, max) = (7141,911, 7575,555, 7957,365), stdev = 271,682
  CI (99.9%): [6813,707, 8337,402] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=50853:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 1 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixSequenceMapFilterToListBenchmark.sequenceOfListMapFilterToList
# Parameters: (nrOfIterations = 100000)

# Run progress: 50,00% complete, ETA 00:02:44
# Fork: 1 of 2
# Warmup Iteration   1: 1957,887 ops/s
Iteration   1: 1936,447 ops/s
Iteration   2: 1982,581 ops/s
Iteration   3: 1943,866 ops/s

# Run progress: 62,50% complete, ETA 00:02:03
# Fork: 2 of 2
# Warmup Iteration   1: 1914,251 ops/s
Iteration   1: 1972,322 ops/s
Iteration   2: 1667,698 ops/s
Iteration   3: 1607,212 ops/s


Result "benchmark.prefix.PrefixSequenceMapFilterToListBenchmark.sequenceOfListMapFilterToList":
  1851,688 ±(99.9%) 470,881 ops/s [Average]
  (min, avg, max) = (1607,212, 1851,688, 1982,581), stdev = 167,921
  CI (99.9%): [1380,806, 2322,569] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=50853:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 1 iterations, 10 s each
# Measurement: 3 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixSequenceMapFilterToListBenchmark.streamMapFilterToList
# Parameters: (nrOfIterations = 100000)

# Run progress: 75,00% complete, ETA 00:01:22
# Fork: 1 of 2
# Warmup Iteration   1: 2507,994 ops/s
Iteration   1: 2235,788 ops/s
Iteration   2: 2210,122 ops/s
Iteration   3: 2098,302 ops/s

# Run progress: 87,50% complete, ETA 00:00:40
# Fork: 2 of 2
# Warmup Iteration   1: 2658,778 ops/s
Iteration   1: 2379,436 ops/s
Iteration   2: 2453,063 ops/s
Iteration   3: 2447,680 ops/s


Result "benchmark.prefix.PrefixSequenceMapFilterToListBenchmark.streamMapFilterToList":
  2304,065 ±(99.9%) 405,070 ops/s [Average]
  (min, avg, max) = (2098,302, 2304,065, 2453,063), stdev = 144,452
  CI (99.9%): [1898,995, 2709,135] (assumes normal distribution)


# Run complete. Total time: 00:05:28

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

Benchmark                                                             (nrOfIterations)   Mode  Cnt     Score     Error  Units
PrefixSequenceMapFilterToListBenchmark.imperativeMapFilterToList                100000  thrpt    6  3938,669 ± 297,466  ops/s
PrefixSequenceMapFilterToListBenchmark.parallelStreamMapFilterToList            100000  thrpt    6  7575,555 ± 761,848  ops/s
PrefixSequenceMapFilterToListBenchmark.sequenceOfListMapFilterToList            100000  thrpt    6  1851,688 ± 470,881  ops/s
PrefixSequenceMapFilterToListBenchmark.streamMapFilterToList                    100000  thrpt    6  2304,065 ± 405,070  ops/s

Process finished with exit code 0

````
