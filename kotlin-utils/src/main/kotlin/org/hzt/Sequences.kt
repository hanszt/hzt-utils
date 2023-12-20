package org.hzt

import java.util.stream.Collector
import org.hzt.utils.gatherers.Gatherer
import org.hzt.utils.iterators.Iterators

fun <T, A, R> Sequence<T>.gather(gatherer: Gatherer<T, A, R>): Sequence<R> =
    Sequence { Iterators.gatheringIterator(iterator(), gatherer) }

fun <T, A1, A2, R1, R2> Sequence<T>.teeing(
    collector1: Collector<T, A1, R1>,
    collector2: Collector<T, A2, R2>
): Pair<R1, R2> {
    val container1 = collector1.supplier().get()
    val accumulator1 = collector1.accumulator()
    val container2 = collector2.supplier().get()
    val accumulator2 = collector2.accumulator()
    val iterator = iterator()
    while (iterator.hasNext()) {
        val next = iterator.next()
        accumulator1.accept(container1, next)
        accumulator2.accept(container2, next)
    }
    return collector1.finisher().apply(container1) to collector2.finisher().apply(container2)
}

fun <T, A1, A2, A3, R1, R2, R3> Sequence<T>.teeing(
    collector1: Collector<T, A1, R1>,
    collector2: Collector<T, A2, R2>,
    collector3: Collector<T, A3, R3>
): Triple<R1, R2, R3> {
    val container1 = collector1.supplier().get()
    val accumulator1 = collector1.accumulator()
    val container2 = collector2.supplier().get()
    val accumulator2 = collector2.accumulator()
    val container3 = collector3.supplier().get()
    val accumulator3 = collector3.accumulator()
    val iterator = iterator()
    while (iterator.hasNext()) {
        val next = iterator.next()
        accumulator1.accept(container1, next)
        accumulator2.accept(container2, next)
        accumulator3.accept(container3, next)
    }
    val r1 = collector1.finisher().apply(container1)
    val r2 = collector2.finisher().apply(container2)
    val r3 = collector3.finisher().apply(container3)
    return Triple(r1, r2, r3)
}
