package org.hzt

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import java.util.stream.Collectors.mapping
import java.util.stream.Collectors.toList
import java.util.stream.Collectors.toUnmodifiableList
import org.hzt.utils.gatherers.GatherersX.runningIntStatisticsOf
import org.hzt.utils.statistics.IntStatistics
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SequencesKtTest {

    @Test
    fun `sequence gather`() {
        val sequence = sequenceOf(1, 3, 5, 3, 8, 1, 2, 3)
        val (maxes, sums) = sequence
            .gather(runningIntStatisticsOf { it })
            .teeing(
                mapping({ it.max }, toList()),
                mapping({ it.sum }, toList())
            )

        assertSoftly {
            maxes shouldBe listOf(1, 3, 5, 5, 8, 8, 8, 8)
            sums shouldBe listOf(1L, 4L, 9L, 12L, 20L, 21L, 23L, 26L)
            sequence.runningStatistics().map { it.sum }.toList() shouldBe sums
        }
    }

    @Test
    fun `sequence gather teeing to three`() {
        val (mins, maxes, sums) = sequenceOf(2, 3, 5, 3, 8, 1, 2, 3)
            .gather(runningIntStatisticsOf { it })
            .teeing(
                mapping({ it.min }, toUnmodifiableList()),
                mapping({ it.max }, toUnmodifiableList()),
                mapping({ it.sum }, toUnmodifiableList()),
            )

        assertSoftly {
            mins shouldBe listOf(2, 2, 2, 2, 2, 1, 1, 1)
            maxes shouldBe listOf(2, 3, 5, 5, 8, 8, 8, 8)
            sums shouldBe listOf(2L, 5L, 10L, 13L, 21L, 22L, 24L, 27L)
        }
    }

    @Nested
    inner class ToUnmodifiableCollectionsTests {

        @Test
        fun `to unmodifiable list should really return an unmodifiable list`() {
            val sequence = sequenceOf("This", "is", "unmodifiable")
            val unmodifiableList = sequence.toUnmodifiableList()
            val list = sequence.toList()

            assertSoftly {
                unmodifiableList shouldBe listOf("This", "is", "unmodifiable")
                shouldThrow<UnsupportedOperationException> { (unmodifiableList as MutableList).add("not") }
                shouldThrow<UnsupportedOperationException> { (unmodifiableList as MutableList).removeAt(1) }
                shouldThrow<UnsupportedOperationException> { (unmodifiableList as MutableList).set(2, "test") }
                shouldThrow<UnsupportedOperationException> { (unmodifiableList as MutableList).sort() }
                shouldThrow<UnsupportedOperationException> { (unmodifiableList as MutableList).addLast("s") }
                (list as MutableList)[2] = "test"
                list.removeAt(1)
                list.add("not")
                list shouldBe listOf("This", "test", "not")
            }
        }

        @Test
        fun `to unmodifiable set should really return an unmodifiable set`() {
            val sequence = sequenceOf("This", "is", "unmodifiable")
            val unmodifiableSet = sequence.toUnmodifiableSet()
            val set = sequence.toSet()

            assertSoftly {
                shouldThrow<UnsupportedOperationException> { (unmodifiableSet as MutableSet).add("not") }
                shouldThrow<UnsupportedOperationException> { (unmodifiableSet as MutableSet).remove("is") }
                shouldThrow<UnsupportedOperationException> { (unmodifiableSet as MutableSet).removeIf { it.startsWith('i') } }
                (set as MutableSet).add("not")
                set.remove("unmodifiable")
                unmodifiableSet shouldBe setOf("This", "is", "unmodifiable")
                set shouldBe setOf("This", "is", "not")
            }
        }

        @Test
        fun `associateUnmodifiable should really return an unmodifiable map`() {
            val sequence = sequenceOf("This", "is", "unmodifiable")
            val unmodifiableMap = sequence.associateUnmodifiable { it to it.length }
            val map = sequence.associateWith { it.length }
            assertSoftly {
                shouldThrow<UnsupportedOperationException> { (unmodifiableMap as MutableMap).put("not", 3) }
                shouldThrow<UnsupportedOperationException> { (unmodifiableMap as MutableMap).remove("is") }
                (map as MutableMap)["not"] = 2
                map.remove("unmodifiable")
                unmodifiableMap shouldBe mapOf("This" to 4, "is" to 2, "unmodifiable" to 12)
                map shouldBe mapOf("This" to 4, "is" to 2, "not" to 2)
            }
        }
    }
}

fun Sequence<Int>.runningStatistics(): Sequence<IntStatistics> = Sequence {
    val source = iterator()
    val stats = IntStatistics()
    object : Iterator<IntStatistics> {
        override fun hasNext(): Boolean = source.hasNext()
        override fun next(): IntStatistics = IntStatistics().combine(stats.also { it.accept(source.next()) })
    }
}