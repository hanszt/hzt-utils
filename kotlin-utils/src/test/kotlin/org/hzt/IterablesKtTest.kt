package org.hzt

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class IterablesKtTest {

    @Test
    fun `flatMap array of arrays`() {
        val arrays = arrayOf(arrayOf("test"), arrayOf("other test"))

        val expected = arrays.flatMap { it.map(String::length) }
        val actual = arrays.flatMapArray(String::length)

        actual shouldBe expected
    }

    @Test
    fun `flatMap Iterable of arrays`() {
        val arrays = listOf(arrayOf("test"), arrayOf("other test"))

        val expected = arrays.flatMap { it.map(String::length) }
        val actual = arrays.flatMapArray(String::length)

        actual shouldBe expected
    }
}