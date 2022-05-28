```
"C:\Program Files\Java\jdk-17\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=57715:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin" -Dfile.encoding=UTF-8 -classpath C:\_Programming\Java\hzt-utils\benchmark\target\classes;C:\_Programming\Java\hzt-utils\core\target\classes;C:\Users\hzuiderv\.m2\repository\org\jetbrains\annotations\22.0.0\annotations-22.0.0.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-core\1.34\jmh-core-1.34.jar;C:\Users\hzuiderv\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\hzuiderv\.m2\repository\org\apache\commons\commons-math3\3.2\commons-math3-3.2.jar;C:\Users\hzuiderv\.m2\repository\org\openjdk\jmh\jmh-generator-annprocess\1.34\jmh-generator-annprocess-1.34.jar benchmark.prefix.PrefixSequenceGroupingBenchmark
# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=57715:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 5 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixSequenceGroupingBenchmark.groupByImperative
# Parameters: (nrOfIterations = 100000)

# Run progress: 0,00% complete, ETA 00:33:20
# Fork: 1 of 5
# Warmup Iteration   1: 2252,858 ops/s
# Warmup Iteration   2: 2313,962 ops/s
# Warmup Iteration   3: 2353,725 ops/s
# Warmup Iteration   4: 2390,825 ops/s
# Warmup Iteration   5: 2337,108 ops/s
Iteration   1: 2061,100 ops/s
Iteration   2: 2052,621 ops/s
Iteration   3: 2054,312 ops/s
Iteration   4: 2074,228 ops/s
Iteration   5: 2065,350 ops/s

# Run progress: 5,00% complete, ETA 00:32:04
# Fork: 2 of 5
# Warmup Iteration   1: 2409,270 ops/s
# Warmup Iteration   2: 2431,977 ops/s
# Warmup Iteration   3: 2416,812 ops/s
# Warmup Iteration   4: 2446,295 ops/s
# Warmup Iteration   5: 2373,002 ops/s
Iteration   1: 2063,433 ops/s
Iteration   2: 2051,182 ops/s
Iteration   3: 2076,385 ops/s
Iteration   4: 2063,327 ops/s
Iteration   5: 2071,450 ops/s

# Run progress: 10,00% complete, ETA 00:30:22
# Fork: 3 of 5
# Warmup Iteration   1: 2401,674 ops/s
# Warmup Iteration   2: 2424,985 ops/s
# Warmup Iteration   3: 2403,780 ops/s
# Warmup Iteration   4: 2429,300 ops/s
# Warmup Iteration   5: 2359,276 ops/s
Iteration   1: 2077,707 ops/s
Iteration   2: 2069,519 ops/s
Iteration   3: 2070,061 ops/s
Iteration   4: 2048,631 ops/s
Iteration   5: 2045,171 ops/s

# Run progress: 15,00% complete, ETA 00:28:40
# Fork: 4 of 5
# Warmup Iteration   1: 2361,083 ops/s
# Warmup Iteration   2: 2313,023 ops/s
# Warmup Iteration   3: 2287,117 ops/s
# Warmup Iteration   4: 2372,380 ops/s
# Warmup Iteration   5: 2347,642 ops/s
Iteration   1: 2045,260 ops/s
Iteration   2: 2055,781 ops/s
Iteration   3: 2053,638 ops/s
Iteration   4: 2065,966 ops/s
Iteration   5: 2080,323 ops/s

# Run progress: 20,00% complete, ETA 00:26:58
# Fork: 5 of 5
# Warmup Iteration   1: 2325,069 ops/s
# Warmup Iteration   2: 2344,964 ops/s
# Warmup Iteration   3: 2343,933 ops/s
# Warmup Iteration   4: 2377,024 ops/s
# Warmup Iteration   5: 2346,347 ops/s
Iteration   1: 2034,562 ops/s
Iteration   2: 2059,900 ops/s
Iteration   3: 2067,333 ops/s
Iteration   4: 2065,049 ops/s
Iteration   5: 2076,711 ops/s


Result "benchmark.prefix.PrefixSequenceGroupingBenchmark.groupByImperative":
  2061,960 ±(99.9%) 8,794 ops/s [Average]
  (min, avg, max) = (2034,562, 2061,960, 2080,323), stdev = 11,739
  CI (99.9%): [2053,166, 2070,754] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=57715:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 5 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixSequenceGroupingBenchmark.intRangeFilterGroup
# Parameters: (nrOfIterations = 100000)

# Run progress: 25,00% complete, ETA 00:25:17
# Fork: 1 of 5
# Warmup Iteration   1: 1765,354 ops/s
# Warmup Iteration   2: 1780,281 ops/s
# Warmup Iteration   3: 1764,357 ops/s
# Warmup Iteration   4: 1781,367 ops/s
# Warmup Iteration   5: 1785,307 ops/s
Iteration   1: 1792,168 ops/s
Iteration   2: 1766,247 ops/s
Iteration   3: 1764,578 ops/s
Iteration   4: 1777,274 ops/s
Iteration   5: 1790,826 ops/s

# Run progress: 30,00% complete, ETA 00:23:35
# Fork: 2 of 5
# Warmup Iteration   1: 1584,471 ops/s
# Warmup Iteration   2: 1599,668 ops/s
# Warmup Iteration   3: 1596,341 ops/s
# Warmup Iteration   4: 1602,089 ops/s
# Warmup Iteration   5: 1566,593 ops/s
Iteration   1: 1577,456 ops/s
Iteration   2: 1572,968 ops/s
Iteration   3: 1588,569 ops/s
Iteration   4: 1591,172 ops/s
Iteration   5: 1602,190 ops/s

# Run progress: 35,00% complete, ETA 00:21:54
# Fork: 3 of 5
# Warmup Iteration   1: 1562,480 ops/s
# Warmup Iteration   2: 1554,409 ops/s
# Warmup Iteration   3: 1562,710 ops/s
# Warmup Iteration   4: 1580,071 ops/s
# Warmup Iteration   5: 1581,534 ops/s
Iteration   1: 1553,698 ops/s
Iteration   2: 1558,794 ops/s
Iteration   3: 1563,678 ops/s
Iteration   4: 1568,765 ops/s
Iteration   5: 1565,054 ops/s

# Run progress: 40,00% complete, ETA 00:20:13
# Fork: 4 of 5
# Warmup Iteration   1: 1593,971 ops/s
# Warmup Iteration   2: 1574,681 ops/s
# Warmup Iteration   3: 1572,327 ops/s
# Warmup Iteration   4: 1589,291 ops/s
# Warmup Iteration   5: 1585,190 ops/s
Iteration   1: 1591,402 ops/s
Iteration   2: 1585,551 ops/s
Iteration   3: 1598,383 ops/s
Iteration   4: 1592,007 ops/s
Iteration   5: 1597,768 ops/s

# Run progress: 45,00% complete, ETA 00:18:32
# Fork: 5 of 5
# Warmup Iteration   1: 1771,163 ops/s
# Warmup Iteration   2: 1749,088 ops/s
# Warmup Iteration   3: 1765,381 ops/s
# Warmup Iteration   4: 1775,013 ops/s
# Warmup Iteration   5: 1768,534 ops/s
Iteration   1: 1771,948 ops/s
Iteration   2: 1789,000 ops/s
Iteration   3: 1785,688 ops/s
Iteration   4: 1787,771 ops/s
Iteration   5: 1784,714 ops/s


Result "benchmark.prefix.PrefixSequenceGroupingBenchmark.intRangeFilterGroup":
  1660,707 ±(99.9%) 75,787 ops/s [Average]
  (min, avg, max) = (1553,698, 1660,707, 1792,168), stdev = 101,174
  CI (99.9%): [1584,919, 1736,494] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=57715:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 5 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixSequenceGroupingBenchmark.intStreamRangeFilterGroup
# Parameters: (nrOfIterations = 100000)

# Run progress: 50,00% complete, ETA 00:16:51
# Fork: 1 of 5
# Warmup Iteration   1: 1187,970 ops/s
# Warmup Iteration   2: 1268,811 ops/s
# Warmup Iteration   3: 1273,112 ops/s
# Warmup Iteration   4: 1277,199 ops/s
# Warmup Iteration   5: 1273,532 ops/s
Iteration   1: 1271,597 ops/s
Iteration   2: 1266,137 ops/s
Iteration   3: 1252,728 ops/s
Iteration   4: 1278,082 ops/s
Iteration   5: 1278,100 ops/s

# Run progress: 55,00% complete, ETA 00:15:09
# Fork: 2 of 5
# Warmup Iteration   1: 1194,238 ops/s
# Warmup Iteration   2: 1273,173 ops/s
# Warmup Iteration   3: 1270,327 ops/s
# Warmup Iteration   4: 1269,218 ops/s
# Warmup Iteration   5: 1280,649 ops/s
Iteration   1: 1275,946 ops/s
Iteration   2: 1267,729 ops/s
Iteration   3: 1282,641 ops/s
Iteration   4: 1269,846 ops/s
Iteration   5: 1274,734 ops/s

# Run progress: 60,00% complete, ETA 00:13:28
# Fork: 3 of 5
# Warmup Iteration   1: 1198,867 ops/s
# Warmup Iteration   2: 1269,079 ops/s
# Warmup Iteration   3: 1276,227 ops/s
# Warmup Iteration   4: 1278,534 ops/s
# Warmup Iteration   5: 1261,747 ops/s
Iteration   1: 1270,948 ops/s
Iteration   2: 1270,684 ops/s
Iteration   3: 1284,986 ops/s
Iteration   4: 1267,179 ops/s
Iteration   5: 1272,326 ops/s

# Run progress: 65,00% complete, ETA 00:11:47
# Fork: 4 of 5
# Warmup Iteration   1: 1173,894 ops/s
# Warmup Iteration   2: 1260,052 ops/s
# Warmup Iteration   3: 1281,148 ops/s
# Warmup Iteration   4: 1263,373 ops/s
# Warmup Iteration   5: 1256,186 ops/s
Iteration   1: 1277,258 ops/s
Iteration   2: 1261,053 ops/s
Iteration   3: 1218,053 ops/s
Iteration   4: 1278,545 ops/s
Iteration   5: 1272,034 ops/s

# Run progress: 70,00% complete, ETA 00:10:06
# Fork: 5 of 5
# Warmup Iteration   1: 1200,133 ops/s
# Warmup Iteration   2: 1267,060 ops/s
# Warmup Iteration   3: 1267,934 ops/s
# Warmup Iteration   4: 1261,347 ops/s
# Warmup Iteration   5: 1277,479 ops/s
Iteration   1: 1279,231 ops/s
Iteration   2: 1282,146 ops/s
Iteration   3: 1281,031 ops/s
Iteration   4: 1269,279 ops/s
Iteration   5: 1273,590 ops/s


Result "benchmark.prefix.PrefixSequenceGroupingBenchmark.intStreamRangeFilterGroup":
  1271,035 ±(99.9%) 9,860 ops/s [Average]
  (min, avg, max) = (1218,053, 1271,035, 1284,986), stdev = 13,163
  CI (99.9%): [1261,175, 1280,895] (assumes normal distribution)


# JMH version: 1.34
# VM version: JDK 17, Java HotSpot(TM) 64-Bit Server VM, 17+35-LTS-2724
# VM invoker: C:\Program Files\Java\jdk-17\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\lib\idea_rt.jar=57715:C:\Program Files\JetBrains\IntelliJ IDEA 2021.2.2\bin -Dfile.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 5 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: benchmark.prefix.PrefixSequenceGroupingBenchmark.parallelIntStreamRangeFilterGroup
# Parameters: (nrOfIterations = 100000)

# Run progress: 75,00% complete, ETA 00:08:25
# Fork: 1 of 5
# Warmup Iteration   1: 6290,652 ops/s
# Warmup Iteration   2: 6230,061 ops/s
# Warmup Iteration   3: 6148,051 ops/s
# Warmup Iteration   4: 6083,815 ops/s
# Warmup Iteration   5: 5804,799 ops/s
Iteration   1: 5906,983 ops/s
Iteration   2: 5631,652 ops/s
Iteration   3: 5684,241 ops/s
Iteration   4: 5873,340 ops/s
Iteration   5: 5802,986 ops/s

# Run progress: 80,00% complete, ETA 00:06:44
# Fork: 2 of 5
# Warmup Iteration   1: 5509,808 ops/s
# Warmup Iteration   2: 5323,863 ops/s
# Warmup Iteration   3: 5405,487 ops/s
# Warmup Iteration   4: 5513,545 ops/s
# Warmup Iteration   5: 5420,354 ops/s
Iteration   1: 5403,228 ops/s
Iteration   2: 5443,993 ops/s
Iteration   3: 5416,597 ops/s
Iteration   4: 5422,389 ops/s
Iteration   5: 5631,266 ops/s

# Run progress: 85,00% complete, ETA 00:05:03
# Fork: 3 of 5
# Warmup Iteration   1: 6006,213 ops/s
# Warmup Iteration   2: 6226,169 ops/s
# Warmup Iteration   3: 6109,721 ops/s
# Warmup Iteration   4: 6251,478 ops/s
# Warmup Iteration   5: 6250,720 ops/s
Iteration   1: 6244,204 ops/s
Iteration   2: 6238,085 ops/s
Iteration   3: 6239,223 ops/s
Iteration   4: 6125,495 ops/s
Iteration   5: 6240,746 ops/s

# Run progress: 90,00% complete, ETA 00:03:22
# Fork: 4 of 5
# Warmup Iteration   1: 5897,349 ops/s
# Warmup Iteration   2: 5859,522 ops/s
# Warmup Iteration   3: 5887,564 ops/s
# Warmup Iteration   4: 5939,532 ops/s
# Warmup Iteration   5: 5797,408 ops/s
Iteration   1: 5933,245 ops/s
Iteration   2: 5867,619 ops/s
Iteration   3: 5915,251 ops/s
Iteration   4: 5698,701 ops/s
Iteration   5: 5760,080 ops/s

# Run progress: 95,00% complete, ETA 00:01:41
# Fork: 5 of 5
# Warmup Iteration   1: 5495,178 ops/s
# Warmup Iteration   2: 5563,932 ops/s
# Warmup Iteration   3: 5924,365 ops/s
# Warmup Iteration   4: 5937,474 ops/s
# Warmup Iteration   5: 5926,684 ops/s
Iteration   1: 5927,841 ops/s
Iteration   2: 5887,819 ops/s
Iteration   3: 5878,940 ops/s
Iteration   4: 5898,387 ops/s
Iteration   5: 5796,274 ops/s


Result "benchmark.prefix.PrefixSequenceGroupingBenchmark.parallelIntStreamRangeFilterGroup":
  5834,743 ±(99.9%) 193,437 ops/s [Average]
  (min, avg, max) = (5403,228, 5834,743, 6244,204), stdev = 258,233
  CI (99.9%): [5641,306, 6028,181] (assumes normal distribution)


# Run complete. Total time: 00:33:41

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

Benchmark                                                          (nrOfIterations)   Mode  Cnt     Score     Error  Units
PrefixSequenceGroupingBenchmark.groupByImperative                            100000  thrpt   25  2061,960 ±   8,794  ops/s
PrefixSequenceGroupingBenchmark.intRangeFilterGroup                          100000  thrpt   25  1660,707 ±  75,787  ops/s
PrefixSequenceGroupingBenchmark.intStreamRangeFilterGroup                    100000  thrpt   25  1271,035 ±   9,860  ops/s
PrefixSequenceGroupingBenchmark.parallelIntStreamRangeFilterGroup            100000  thrpt   25  5834,743 ± 193,437  ops/s

Process finished with exit code 0

```
