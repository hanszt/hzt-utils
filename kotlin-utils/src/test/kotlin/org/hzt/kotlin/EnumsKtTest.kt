package org.hzt.kotlin

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import org.junit.jupiter.api.Test
import java.time.Month

class EnumsKtTest {

    @Test
    fun `should return an empty enumSet`() {
        emptyEnumSet<Month>().shouldBeEmpty()
    }

    @Test
    fun `should return an enumSet with all months`() {
        enumSetAllOf<Month>() shouldHaveSize 12
    }
}
