package org.hzt

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import java.time.Month
import org.junit.jupiter.api.Test

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
