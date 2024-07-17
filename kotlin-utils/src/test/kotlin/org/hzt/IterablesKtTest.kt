package org.hzt

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
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

    @Test
    fun `map to truly immutable list`() {
        val strings = listOf("test", "other test", "last")
        val lengths = strings.mapUnmodifiable(String::length)

        assertSoftly {
            lengths shouldBe listOf(4, 10, 4)
            shouldThrow<UnsupportedOperationException> { (lengths as MutableList).add(2) }
            shouldThrow<UnsupportedOperationException> { (lengths as MutableList).removeAt(1) }
            shouldThrow<UnsupportedOperationException> { (lengths as MutableList).set(2, 5) }
            shouldThrow<UnsupportedOperationException> { (lengths as MutableList).sort() }
            shouldThrow<UnsupportedOperationException> { (lengths as MutableList).addLast(2) }
        }
    }
}