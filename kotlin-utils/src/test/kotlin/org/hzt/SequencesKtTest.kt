package org.hzt

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import java.util.stream.Collectors.mapping
import java.util.stream.Collectors.toList
import org.hzt.utils.gatherers.GatherersX.runningIntStatisticsOf
import org.junit.jupiter.api.Test

class SequencesKtTest {

    @Test
    fun `sequence gather`() {
        val (maxes, sums) = sequenceOf(1, 3, 5, 3, 8, 1, 2, 3)
            .gather(runningIntStatisticsOf { it })
            .teeing(
                mapping({ it.max }, toList()),
                mapping({ it.sum }, toList())
            )

        assertSoftly {
            maxes shouldBe listOf(1, 3, 5, 5, 8, 8, 8, 8)
            sums shouldBe listOf(1L, 4L, 9L, 12L, 20L, 21L, 23L, 26L)
        }
    }

    @Test
    fun `sequence gather teeing to three`() {
        val (mins, maxes, sums) = sequenceOf(1, 3, 5, 3, 8, 1, 2, 3)
            .gather(runningIntStatisticsOf { it })
            .teeing(
                mapping({ it.min }, toList()),
                mapping({ it.max }, toList()),
                mapping({ it.sum }, toList()),
            )

        assertSoftly {
            mins shouldBe listOf(1, 1, 1, 1, 1, 1, 1, 1)
            maxes shouldBe listOf(1, 3, 5, 5, 8, 8, 8, 8)
            sums shouldBe listOf(1L, 4L, 9L, 12L, 20L, 21L, 23L, 26L)
        }
    }
}